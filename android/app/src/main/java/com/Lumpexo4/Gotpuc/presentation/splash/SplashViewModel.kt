package com.Lumpexo4.Gotpuc.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lumpexo4.Gotpuc.core.config.GameConfig
import com.Lumpexo4.Gotpuc.domain.repository.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    settingsRepository: SettingsRepository
) : ViewModel() {

    private val _finished = MutableStateFlow(false)
    val finished: StateFlow<Boolean> = _finished.asStateFlow()

    private val _reducedMotion = MutableStateFlow(settingsRepository.settings.value.reducedMotion)
    val reducedMotion: StateFlow<Boolean> = _reducedMotion.asStateFlow()

    init {
        viewModelScope.launch {
            delay(GameConfig.LOADER_DURATION_MS)
            _finished.value = true
        }
    }
}
