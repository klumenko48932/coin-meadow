package com.Lumpexo4.Gotpuc.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lumpexo4.Gotpuc.core.config.GameConfig
import com.Lumpexo4.Gotpuc.data.local.DayProvider
import com.Lumpexo4.Gotpuc.domain.model.DailyChallenge
import com.Lumpexo4.Gotpuc.domain.model.Difficulty
import com.Lumpexo4.Gotpuc.domain.model.RoundResult
import com.Lumpexo4.Gotpuc.domain.model.StrikeVerdict
import com.Lumpexo4.Gotpuc.domain.model.Target
import com.Lumpexo4.Gotpuc.domain.repository.CollectionRepository
import com.Lumpexo4.Gotpuc.domain.repository.ProgressRepository
import com.Lumpexo4.Gotpuc.domain.repository.SettingsRepository
import com.Lumpexo4.Gotpuc.domain.usecase.GetDailyChallengeUseCase
import com.Lumpexo4.Gotpuc.domain.usecase.ResolveStrikeUseCase
import com.Lumpexo4.Gotpuc.domain.usecase.ResolveTapUseCase
import com.Lumpexo4.Gotpuc.domain.usecase.SaveRoundResultUseCase
import com.Lumpexo4.Gotpuc.domain.usecase.SpawnTargetUseCase
import com.Lumpexo4.Gotpuc.domain.usecase.UnlockCharmsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val collectionRepository: CollectionRepository,
    private val resolveTap: ResolveTapUseCase,
    private val resolveStrike: ResolveStrikeUseCase,
    private val spawnTarget: SpawnTargetUseCase,
    private val saveRoundResult: SaveRoundResultUseCase,
    private val unlockCharms: UnlockCharmsUseCase,
    private val getDailyChallenge: GetDailyChallengeUseCase,
    private val dayProvider: DayProvider
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val eventChannel = Channel<RoundResult>(Channel.BUFFERED)
    val events: Flow<RoundResult> = eventChannel.receiveAsFlow()

    private var loopJob: Job? = null
    private var clockMs = 0L
    private var lastSpawnMs = 0L
    private var spawnIndex = 0
    private var nextTargetId = 1L
    private var nextBurstId = 1L
    private var burstAtMs = -1L
    private var bannerAtMs = -1L
    private var boostUntilMs = -1L
    private var perfectStrikes = 0
    private var strongStrikes = 0
    private var baselineBest = 0
    private var challenge: DailyChallenge? = null
    private var difficulty = Difficulty.Meadow
    private val bankedCharms = mutableListOf<Int>()

    fun startRound(daily: Boolean) {
        loopJob?.cancel()
        val settings = settingsRepository.settings.value
        val progress = progressRepository.progress.value
        challenge = if (daily) getDailyChallenge(dayProvider.dayOfYear()) else null
        difficulty = settings.difficulty
        clockMs = 0L
        lastSpawnMs = 0L
        spawnIndex = 0
        burstAtMs = -1L
        bannerAtMs = -1L
        boostUntilMs = -1L
        perfectStrikes = 0
        strongStrikes = 0
        baselineBest = progress.bestScore
        bankedCharms.clear()
        val duration = challenge?.durationMs ?: GameConfig.ROUND_DURATION_MS
        val lives = challenge?.lives ?: GameConfig.MAX_LIVES
        _state.value = GameUiState(
            phase = GamePhase.Idle,
            lives = lives,
            maxLives = lives,
            millisLeft = duration,
            totalMillis = duration,
            boostCharges = progress.boostCharges,
            strikeBand = bandFor(1),
            reducedMotion = settings.reducedMotion,
            dailyNameRes = challenge?.nameRes
        )
        startLoop()
    }

    fun pause() {
        val phase = _state.value.phase
        if (phase != GamePhase.Idle && phase != GamePhase.Playing) return
        loopJob?.cancel()
        loopJob = null
        _state.update { it.copy(phase = GamePhase.Paused, burst = null) }
    }

    fun resume() {
        if (_state.value.phase != GamePhase.Paused) return
        _state.update { it.copy(phase = GamePhase.Playing) }
        startLoop()
    }

    fun useBoost() {
        val current = _state.value
        if (current.phase != GamePhase.Playing || current.boostCharges <= 0) return
        progressRepository.spendBoostCharge()
        boostUntilMs = clockMs + GameConfig.BOOST_DURATION_MS
        _state.update {
            it.copy(boostCharges = it.boostCharges - 1, boostActive = true)
        }
    }

    fun onTargetTap(targetId: Long) {
        val current = _state.value
        if (current.phase != GamePhase.Playing) return
        val target = current.targets.firstOrNull { it.id == targetId } ?: return
        val unlocked = collectionRepository.unlockedCharmIds.value + bankedCharms
        val nextCharm = unlockCharms.nextLocked(unlocked)
        val outcome = resolveTap(target.kind, current.multiplier, current.lives, nextCharm != null)

        if (outcome.charmBanked && nextCharm != null) {
            bankedCharms.add(nextCharm)
        }

        val coins = current.coins + outcome.coinsGained
        val streak = when {
            outcome.streakReset -> 0
            outcome.streakUp -> current.streak + 1
            else -> current.streak
        }
        val lives = (current.lives + (if (outcome.lifeRestored) 1 else 0) -
            (if (outcome.lifeLost) 1 else 0)).coerceIn(0, current.maxLives)
        val millis = (current.millisLeft + outcome.timeGainedMs)
            .coerceAtMost(current.totalMillis + EXTRA_TIME_CAP_MS)
        val level = levelFor(coins)
        val levelUp = level > current.level
        if (levelUp) bannerAtMs = clockMs
        val burst = if (outcome.coinsGained > 0) {
            burstAtMs = clockMs
            HitBurst(
                id = nextBurstId++,
                cell = target.cell,
                amount = outcome.coinsGained,
                sparkle = streak >= GameConfig.SPARKLE_STREAK_MIN
            )
        } else {
            current.burst
        }

        _state.update {
            it.copy(
                targets = it.targets.filterNot { item -> item.id == targetId },
                coins = coins,
                streak = streak,
                bestStreak = maxOf(it.bestStreak, streak),
                lives = lives,
                millisLeft = millis,
                level = level,
                multiplier = multiplierFor(streak),
                strikeBand = bandFor(level),
                burst = burst,
                shakeTick = if (outcome.lifeLost) it.shakeTick + 1 else it.shakeTick,
                levelBanner = if (levelUp) level else it.levelBanner,
                phase = if (outcome.opensStrike) GamePhase.Strike else it.phase,
                strikePot = if (outcome.opensStrike) {
                    maxOf(current.streak, 1) * GameConfig.COIN_VALUE
                } else {
                    it.strikePot
                }
            )
        }

        if (outcome.opensStrike) {
            loopJob?.cancel()
            loopJob = null
            return
        }
        if (lives <= 0) finish()
    }

    fun onStrikeTap(needle: Float) {
        val current = _state.value
        if (current.phase != GamePhase.Strike || current.strikeVerdict != null) return
        val verdict = resolveStrike(needle, current.strikeBand)
        if (verdict == StrikeVerdict.Perfect) perfectStrikes += 1
        if (verdict.multiplier >= STRONG_STRIKE_MULTIPLIER) strongStrikes += 1
        val coins = current.coins + current.strikePot * verdict.multiplier
        val level = levelFor(coins)
        _state.update {
            it.copy(
                coins = coins,
                level = level,
                strikeVerdict = verdict,
                strikeNeedle = needle.coerceIn(0f, 1f),
                topStrike = maxOf(it.topStrike, verdict.multiplier)
            )
        }
        viewModelScope.launch {
            delay(GameConfig.STRIKE_VERDICT_HOLD_MS)
            if (_state.value.phase != GamePhase.Strike) return@launch
            _state.update {
                it.copy(
                    phase = GamePhase.Playing,
                    strikeVerdict = null,
                    strikePot = 0,
                    strikeBand = bandFor(it.level)
                )
            }
            startLoop()
        }
    }

    private fun startLoop() {
        loopJob?.cancel()
        loopJob = viewModelScope.launch {
            while (isActive) {
                delay(GameConfig.TICK_MS)
                tick()
            }
        }
    }

    private fun tick() {
        when (_state.value.phase) {
            GamePhase.Idle -> {
                clockMs += GameConfig.TICK_MS
                if (clockMs >= IDLE_MS) {
                    clockMs = 0L
                    lastSpawnMs = -FIRST_SPAWN_OFFSET_MS
                    _state.update { it.copy(phase = GamePhase.Playing) }
                }
            }

            GamePhase.Playing -> advance()

            else -> Unit
        }
    }

    private fun advance() {
        clockMs += GameConfig.TICK_MS
        val current = _state.value
        val boostActive = clockMs < boostUntilMs
        var targets = current.targets.filter { clockMs - it.bornAtMs < it.lifetimeMs }
        val interval = spawnInterval(current.level, boostActive)
        if (targets.size < GameConfig.MAX_CONCURRENT_TARGETS && clockMs - lastSpawnMs >= interval) {
            val plan = spawnTarget(targets.map { it.cell }.toSet(), spawnIndex, challenge)
            if (plan != null) {
                spawnIndex += 1
                lastSpawnMs = clockMs
                targets = targets + Target(
                    id = nextTargetId++,
                    cell = plan.cell,
                    kind = plan.kind,
                    bornAtMs = clockMs,
                    lifetimeMs = lifetimeFor(current.level)
                )
            }
        }
        val remaining = (current.millisLeft - GameConfig.TICK_MS).coerceAtLeast(0L)
        val clearBurst = burstAtMs >= 0L && clockMs - burstAtMs > BURST_HOLD_MS
        val clearBanner = bannerAtMs >= 0L && clockMs - bannerAtMs > GameConfig.LEVEL_BANNER_MS
        _state.update {
            it.copy(
                targets = targets,
                millisLeft = remaining,
                boostActive = boostActive,
                burst = if (clearBurst) null else it.burst,
                levelBanner = if (clearBanner) 0 else it.levelBanner
            )
        }
        if (clearBurst) burstAtMs = -1L
        if (clearBanner) bannerAtMs = -1L
        if (remaining <= 0L) finish()
    }

    private fun finish() {
        loopJob?.cancel()
        loopJob = null
        val current = _state.value
        if (current.phase == GamePhase.Finished) return
        val active = challenge
        val cleared = active != null && (
            (active.coinTarget > 0 && current.coins >= active.coinTarget) ||
                (active.strikeTarget > 0 && strongStrikes >= active.strikeTarget)
            )
        val result = RoundResult(
            coins = current.coins,
            bestStreak = current.bestStreak,
            topStrike = current.topStrike,
            perfectStrikes = perfectStrikes,
            newCharmIds = bankedCharms.toList(),
            newRecord = current.coins > baselineBest && current.coins > 0,
            daily = active != null,
            dailyCleared = cleared
        )
        _state.update {
            it.copy(phase = GamePhase.Finished, targets = emptyList(), burst = null)
        }
        saveRoundResult(result, dayProvider.dayOfYear())
        viewModelScope.launch { eventChannel.send(result) }
    }

    private fun levelFor(coins: Int): Int =
        (1 + coins / GameConfig.COINS_PER_LEVEL).coerceAtMost(GameConfig.MAX_LEVEL)

    private fun multiplierFor(streak: Int): Float = when {
        streak >= 12 -> 3f
        streak >= 7 -> 2f
        streak >= 3 -> 1.5f
        else -> 1f
    }

    private fun bandFor(level: Int): Float {
        val base = when {
            level >= 10 -> GameConfig.STRIKE_BAND_TIGHT
            level >= 6 -> GameConfig.STRIKE_BAND_MEDIUM
            else -> GameConfig.STRIKE_BAND_WIDE
        }
        return (base + difficulty.bandBonus).coerceIn(0.1f, 0.34f)
    }

    private fun spawnInterval(level: Int, boostActive: Boolean): Long {
        val base = (GameConfig.SPAWN_INTERVAL_BASE_MS - (level - 1) * GameConfig.SPAWN_INTERVAL_STEP_MS)
            .coerceAtLeast(GameConfig.SPAWN_INTERVAL_FLOOR_MS)
        return if (boostActive) {
            (base * (1f + GameConfig.BOOST_SLOWDOWN)).toLong()
        } else {
            base
        }
    }

    private fun lifetimeFor(level: Int): Long =
        (GameConfig.TARGET_LIFETIME_BASE_MS - (level - 1) * GameConfig.TARGET_LIFETIME_STEP_MS +
            difficulty.lifetimeBonusMs).coerceAtLeast(GameConfig.TARGET_LIFETIME_FLOOR_MS)

    private companion object {
        const val IDLE_MS = 600L
        const val FIRST_SPAWN_OFFSET_MS = 2000L
        const val BURST_HOLD_MS = 620L
        const val EXTRA_TIME_CAP_MS = 10_000L
        const val STRONG_STRIKE_MULTIPLIER = 3
    }
}
