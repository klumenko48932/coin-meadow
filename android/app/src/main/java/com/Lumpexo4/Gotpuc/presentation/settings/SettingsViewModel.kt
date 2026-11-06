package com.Lumpexo4.Gotpuc.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lumpexo4.Gotpuc.domain.model.Difficulty
import com.Lumpexo4.Gotpuc.domain.repository.SettingsRepository
import com.Lumpexo4.Gotpuc.domain.usecase.ResetProgressUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val resetProgress: ResetProgressUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _state.update { it.copy(settings = settings) }
            }
        }
    }

    fun setSound(enabled: Boolean) = settingsRepository.setSoundEnabled(enabled)

    fun setHaptics(enabled: Boolean) = settingsRepository.setHapticsEnabled(enabled)

    fun setReducedMotion(enabled: Boolean) = settingsRepository.setReducedMotion(enabled)

    fun setDifficulty(difficulty: Difficulty) = settingsRepository.setDifficulty(difficulty)

    fun askReset() {
        _state.update { it.copy(confirmingReset = true) }
    }

    fun cancelReset() {
        _state.update { it.copy(confirmingReset = false) }
    }

    fun confirmReset() {
        resetProgress()
        _state.update { it.copy(confirmingReset = false) }
    }
}
