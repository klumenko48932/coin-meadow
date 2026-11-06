package com.Lumpexo4.Gotpuc.domain.model

data class AppSettings(
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val reducedMotion: Boolean = false,
    val difficulty: Difficulty = Difficulty.Meadow
)
