package com.Lumpexo4.Gotpuc.domain.repository

import com.Lumpexo4.Gotpuc.domain.model.AppSettings
import com.Lumpexo4.Gotpuc.domain.model.Difficulty
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<AppSettings>
    fun setSoundEnabled(enabled: Boolean)
    fun setHapticsEnabled(enabled: Boolean)
    fun setReducedMotion(enabled: Boolean)
    fun setDifficulty(difficulty: Difficulty)
}
