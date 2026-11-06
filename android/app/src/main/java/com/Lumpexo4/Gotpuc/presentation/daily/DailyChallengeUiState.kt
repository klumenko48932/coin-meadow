package com.Lumpexo4.Gotpuc.presentation.daily

import com.Lumpexo4.Gotpuc.domain.model.DailyChallenge

enum class DayState {
    Completed,
    Today,
    Future,
    Missed
}

data class DayCell(
    val index: Int,
    val state: DayState
)

data class DailyChallengeUiState(
    val challenge: DailyChallenge? = null,
    val dayOfYear: Int = 0,
    val completedToday: Boolean = false,
    val dailyStreak: Int = 0,
    val week: List<DayCell> = emptyList()
)
