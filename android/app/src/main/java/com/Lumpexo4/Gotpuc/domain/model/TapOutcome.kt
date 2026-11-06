package com.Lumpexo4.Gotpuc.domain.model

data class TapOutcome(
    val coinsGained: Int = 0,
    val timeGainedMs: Long = 0L,
    val lifeRestored: Boolean = false,
    val lifeLost: Boolean = false,
    val streakUp: Boolean = false,
    val streakReset: Boolean = false,
    val opensStrike: Boolean = false,
    val charmBanked: Boolean = false
)
