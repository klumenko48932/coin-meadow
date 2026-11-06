package com.Lumpexo4.Gotpuc.presentation.daily

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lumpexo4.Gotpuc.core.config.GameConfig
import com.Lumpexo4.Gotpuc.data.local.DayProvider
import com.Lumpexo4.Gotpuc.domain.repository.ProgressRepository
import com.Lumpexo4.Gotpuc.domain.usecase.GetDailyChallengeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DailyChallengeViewModel(
    progressRepository: ProgressRepository,
    private val getDailyChallenge: GetDailyChallengeUseCase,
    private val dayProvider: DayProvider
) : ViewModel() {

    private val _state = MutableStateFlow(DailyChallengeUiState())
    val state: StateFlow<DailyChallengeUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.progress.collect { progress ->
                val today = dayProvider.dayOfYear()
                val todayIndex = dayProvider.dayOfWeekIndex()
                val week = (0 until GameConfig.DAILY_WEEK_DAYS).map { index ->
                    val day = today - (todayIndex - index)
                    val dayState = when {
                        progress.dailyClearedDays.contains(day) -> DayState.Completed
                        index == todayIndex -> DayState.Today
                        index > todayIndex -> DayState.Future
                        else -> DayState.Missed
                    }
                    DayCell(index = index, state = dayState)
                }
                _state.update {
                    it.copy(
                        challenge = getDailyChallenge(today),
                        dayOfYear = today,
                        completedToday = progress.dailyClearedDays.contains(today),
                        dailyStreak = progress.dailyStreak,
                        week = week
                    )
                }
            }
        }
    }
}
