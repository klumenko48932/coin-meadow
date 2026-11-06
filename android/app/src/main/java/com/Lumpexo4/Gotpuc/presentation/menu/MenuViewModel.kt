package com.Lumpexo4.Gotpuc.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lumpexo4.Gotpuc.data.local.DayProvider
import com.Lumpexo4.Gotpuc.domain.repository.ProgressRepository
import com.Lumpexo4.Gotpuc.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    progressRepository: ProgressRepository,
    settingsRepository: SettingsRepository,
    private val dayProvider: DayProvider
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                progressRepository.progress,
                settingsRepository.settings
            ) { progress, settings -> progress to settings }
                .collect { (progress, settings) ->
                    val today = dayProvider.dayOfYear()
                    _state.update {
                        it.copy(
                            totalCoins = progress.totalCoins,
                            bestScore = progress.bestScore,
                            longestStreak = progress.longestStreak,
                            dailyPending = !progress.dailyClearedDays.contains(today),
                            reducedMotion = settings.reducedMotion
                        )
                    }
                }
        }
    }
}
