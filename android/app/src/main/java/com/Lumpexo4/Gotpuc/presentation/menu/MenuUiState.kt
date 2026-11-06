package com.Lumpexo4.Gotpuc.presentation.menu

data class MenuUiState(
    val totalCoins: Int = 0,
    val bestScore: Int = 0,
    val longestStreak: Int = 0,
    val dailyPending: Boolean = true,
    val reducedMotion: Boolean = false
)
