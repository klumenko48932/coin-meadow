package com.Lumpexo4.Gotpuc.data.repository

import com.Lumpexo4.Gotpuc.data.local.CoinMeadowPreferences
import com.Lumpexo4.Gotpuc.domain.model.AppSettings
import com.Lumpexo4.Gotpuc.domain.model.Difficulty
import com.Lumpexo4.Gotpuc.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(
    private val prefs: CoinMeadowPreferences
) : SettingsRepository {

    private val state = MutableStateFlow(read())

    override val settings: StateFlow<AppSettings> = state.asStateFlow()

    override fun setSoundEnabled(enabled: Boolean) {
        prefs.write { putBoolean(CoinMeadowPreferences.KEY_SOUND, enabled) }
        state.value = state.value.copy(soundEnabled = enabled)
    }

    override fun setHapticsEnabled(enabled: Boolean) {
        prefs.write { putBoolean(CoinMeadowPreferences.KEY_HAPTICS, enabled) }
        state.value = state.value.copy(hapticsEnabled = enabled)
    }

    override fun setReducedMotion(enabled: Boolean) {
        prefs.write { putBoolean(CoinMeadowPreferences.KEY_REDUCED_MOTION, enabled) }
        state.value = state.value.copy(reducedMotion = enabled)
    }

    override fun setDifficulty(difficulty: Difficulty) {
        prefs.write { putInt(CoinMeadowPreferences.KEY_DIFFICULTY, difficulty.ordinal) }
        state.value = state.value.copy(difficulty = difficulty)
    }

    private fun read(): AppSettings {
        val ordinal = prefs.int(CoinMeadowPreferences.KEY_DIFFICULTY, Difficulty.Meadow.ordinal)
        return AppSettings(
            soundEnabled = prefs.bool(CoinMeadowPreferences.KEY_SOUND, true),
            hapticsEnabled = prefs.bool(CoinMeadowPreferences.KEY_HAPTICS, true),
            reducedMotion = prefs.bool(CoinMeadowPreferences.KEY_REDUCED_MOTION, false),
            difficulty = Difficulty.entries.getOrElse(ordinal) { Difficulty.Meadow }
        )
    }
}
