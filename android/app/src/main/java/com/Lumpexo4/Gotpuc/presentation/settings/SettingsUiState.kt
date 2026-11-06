package com.Lumpexo4.Gotpuc.presentation.settings

import com.Lumpexo4.Gotpuc.domain.model.AppSettings

data class SettingsUiState(
    val settings: AppSettings = AppSettings(),
    val confirmingReset: Boolean = false
)
