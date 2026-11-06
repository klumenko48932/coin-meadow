package com.Lumpexo4.Gotpuc.presentation.game

import com.Lumpexo4.Gotpuc.core.config.GameConfig
import com.Lumpexo4.Gotpuc.domain.model.StrikeVerdict
import com.Lumpexo4.Gotpuc.domain.model.Target

enum class GamePhase {
    Idle,
    Playing,
    Strike,
    Paused,
    Finished
}

data class HitBurst(
    val id: Long,
    val cell: Int,
    val amount: Int,
    val sparkle: Boolean
)

data class GameUiState(
    val phase: GamePhase = GamePhase.Idle,
    val targets: List<Target> = emptyList(),
    val coins: Int = 0,
    val streak: Int = 0,
    val bestStreak: Int = 0,
    val lives: Int = GameConfig.MAX_LIVES,
    val maxLives: Int = GameConfig.MAX_LIVES,
    val level: Int = 1,
    val millisLeft: Long = GameConfig.ROUND_DURATION_MS,
    val totalMillis: Long = GameConfig.ROUND_DURATION_MS,
    val multiplier: Float = 1f,
    val boostCharges: Int = 0,
    val boostActive: Boolean = false,
    val strikePot: Int = 0,
    val strikeBand: Float = GameConfig.STRIKE_BAND_WIDE,
    val strikeVerdict: StrikeVerdict? = null,
    val strikeNeedle: Float = 0.5f,
    val topStrike: Int = 0,
    val burst: HitBurst? = null,
    val shakeTick: Int = 0,
    val levelBanner: Int = 0,
    val reducedMotion: Boolean = false,
    val dailyNameRes: Int? = null
) {
    val secondsLeft: Int get() = ((millisLeft + 999L) / 1000L).toInt()
    val timeFraction: Float
        get() = if (totalMillis <= 0L) 0f else (millisLeft.toFloat() / totalMillis.toFloat()).coerceIn(0f, 1f)
}
