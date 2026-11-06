package com.Lumpexo4.Gotpuc.domain.model

data class MeadowLevelProgress(
    val level: MeadowLevel,
    val nextLevelIndex: Int,
    val currentCoins: Int,
    val targetCoins: Int,
    val fraction: Float,
    val maxReached: Boolean
)
