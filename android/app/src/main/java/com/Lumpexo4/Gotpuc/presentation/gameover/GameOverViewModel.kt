package com.Lumpexo4.Gotpuc.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lumpexo4.Gotpuc.domain.repository.ProgressRepository
import com.Lumpexo4.Gotpuc.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameOverUiState(
    val totalCoins: Int = 0,
    val bestScore: Int = 0,
    val reducedMotion: Boolean = false
)

class GameOverViewModel(
    progressRepository: ProgressRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                progressRepository.progress,
                settingsRepository.settings
            ) { progress, settings -> progress to settings }
                .collect { (progress, settings) ->
                    _state.update {
                        it.copy(
                            totalCoins = progress.totalCoins,
                            bestScore = progress.bestScore,
                            reducedMotion = settings.reducedMotion
                        )
                    }
                }
        }
    }
}
