package com.Lumpexo4.Gotpuc.domain.model

data class PlayerProgress(
    val totalCoins: Int = 0,
    val bestScore: Int = 0,
    val longestStreak: Int = 0,
    val roundsPlayed: Int = 0,
    val perfectStrikes: Int = 0,
    val boostCharges: Int = 0,
    val claimedRewardIds: Set<Int> = emptySet(),
    val lastDailyDay: Int = -1,
    val dailyStreak: Int = 0,
    val dailyClearedDays: Set<Int> = emptySet()
)
