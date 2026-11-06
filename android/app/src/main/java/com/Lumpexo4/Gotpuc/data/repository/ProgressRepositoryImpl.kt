package com.Lumpexo4.Gotpuc.data.repository

import com.Lumpexo4.Gotpuc.core.config.GameConfig
import com.Lumpexo4.Gotpuc.data.local.CoinMeadowPreferences
import com.Lumpexo4.Gotpuc.domain.model.PlayerProgress
import com.Lumpexo4.Gotpuc.domain.model.RoundResult
import com.Lumpexo4.Gotpuc.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressRepositoryImpl(
    private val prefs: CoinMeadowPreferences
) : ProgressRepository {

    private val state = MutableStateFlow(read())

    override val progress: StateFlow<PlayerProgress> = state.asStateFlow()

    override fun saveRound(result: RoundResult, dayOfYear: Int) {
        val current = state.value
        val totalCoins = current.totalCoins + result.coins
        val earnedCharges = totalCoins / GameConfig.COINS_PER_BOOST_CHARGE -
            current.totalCoins / GameConfig.COINS_PER_BOOST_CHARGE
        val boost = (current.boostCharges + earnedCharges)
            .coerceIn(0, GameConfig.MAX_BOOST_CHARGES)
        val clearedDaily = result.daily && result.dailyCleared
        val dailyStreak = when {
            !clearedDaily -> current.dailyStreak
            current.lastDailyDay == dayOfYear - 1 -> current.dailyStreak + 1
            current.lastDailyDay == dayOfYear -> current.dailyStreak
            else -> 1
        }
        val updated = current.copy(
            totalCoins = totalCoins,
            bestScore = maxOf(current.bestScore, result.coins),
            longestStreak = maxOf(current.longestStreak, result.bestStreak),
            roundsPlayed = current.roundsPlayed + 1,
            perfectStrikes = current.perfectStrikes + result.perfectStrikes,
            boostCharges = boost,
            lastDailyDay = if (clearedDaily) dayOfYear else current.lastDailyDay,
            dailyStreak = dailyStreak,
            dailyClearedDays = if (clearedDaily) {
                current.dailyClearedDays + dayOfYear
            } else {
                current.dailyClearedDays
            }
        )
        persist(updated)
    }

    override fun claimReward(rewardId: Int, boostCharges: Int) {
        val current = state.value
        if (current.claimedRewardIds.contains(rewardId)) return
        persist(
            current.copy(
                claimedRewardIds = current.claimedRewardIds + rewardId,
                boostCharges = (current.boostCharges + boostCharges)
                    .coerceAtMost(GameConfig.MAX_BOOST_CHARGES)
            )
        )
    }

    override fun spendBoostCharge() {
        val current = state.value
        if (current.boostCharges <= 0) return
        persist(current.copy(boostCharges = current.boostCharges - 1))
    }

    override fun clear() {
        prefs.wipe(
            listOf(
                CoinMeadowPreferences.KEY_TOTAL_COINS,
                CoinMeadowPreferences.KEY_BEST_SCORE,
                CoinMeadowPreferences.KEY_LONGEST_STREAK,
                CoinMeadowPreferences.KEY_ROUNDS_PLAYED,
                CoinMeadowPreferences.KEY_PERFECT_STRIKES,
                CoinMeadowPreferences.KEY_BOOST_CHARGES,
                CoinMeadowPreferences.KEY_CLAIMED_REWARDS,
                CoinMeadowPreferences.KEY_LAST_DAILY_DAY,
                CoinMeadowPreferences.KEY_DAILY_STREAK,
                CoinMeadowPreferences.KEY_DAILY_CLEARED_DAYS
            )
        )
        state.value = PlayerProgress()
    }

    private fun persist(value: PlayerProgress) {
        prefs.write {
            putInt(CoinMeadowPreferences.KEY_TOTAL_COINS, value.totalCoins)
            putInt(CoinMeadowPreferences.KEY_BEST_SCORE, value.bestScore)
            putInt(CoinMeadowPreferences.KEY_LONGEST_STREAK, value.longestStreak)
            putInt(CoinMeadowPreferences.KEY_ROUNDS_PLAYED, value.roundsPlayed)
            putInt(CoinMeadowPreferences.KEY_PERFECT_STRIKES, value.perfectStrikes)
            putInt(CoinMeadowPreferences.KEY_BOOST_CHARGES, value.boostCharges)
            putInt(CoinMeadowPreferences.KEY_LAST_DAILY_DAY, value.lastDailyDay)
            putInt(CoinMeadowPreferences.KEY_DAILY_STREAK, value.dailyStreak)
        }
        prefs.writeIds(CoinMeadowPreferences.KEY_CLAIMED_REWARDS, value.claimedRewardIds)
        prefs.writeIds(CoinMeadowPreferences.KEY_DAILY_CLEARED_DAYS, value.dailyClearedDays)
        state.value = value
    }

    private fun read(): PlayerProgress = PlayerProgress(
        totalCoins = prefs.int(CoinMeadowPreferences.KEY_TOTAL_COINS, 0),
        bestScore = prefs.int(CoinMeadowPreferences.KEY_BEST_SCORE, 0),
        longestStreak = prefs.int(CoinMeadowPreferences.KEY_LONGEST_STREAK, 0),
        roundsPlayed = prefs.int(CoinMeadowPreferences.KEY_ROUNDS_PLAYED, 0),
        perfectStrikes = prefs.int(CoinMeadowPreferences.KEY_PERFECT_STRIKES, 0),
        boostCharges = prefs.int(CoinMeadowPreferences.KEY_BOOST_CHARGES, 0),
        claimedRewardIds = prefs.ids(CoinMeadowPreferences.KEY_CLAIMED_REWARDS),
        lastDailyDay = prefs.int(CoinMeadowPreferences.KEY_LAST_DAILY_DAY, -1),
        dailyStreak = prefs.int(CoinMeadowPreferences.KEY_DAILY_STREAK, 0),
        dailyClearedDays = prefs.ids(CoinMeadowPreferences.KEY_DAILY_CLEARED_DAYS)
    )
}
