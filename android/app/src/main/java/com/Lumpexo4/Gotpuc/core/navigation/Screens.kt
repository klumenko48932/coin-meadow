package com.Lumpexo4.Gotpuc.core.navigation

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data class Game(val daily: Boolean) : Screen
    data class GameOver(
        val coins: Int,
        val bestStreak: Int,
        val topStrike: Int,
        val newCharms: Int,
        val newRecord: Boolean,
        val daily: Boolean,
        val dailyCleared: Boolean
    ) : Screen
    data object Daily : Screen
    data object Progress : Screen
    data object Collection : Screen
    data object Settings : Screen
}
