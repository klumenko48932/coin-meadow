package com.Lumpexo4.Gotpuc.domain.model

data class RoundResult(
    val coins: Int,
    val bestStreak: Int,
    val topStrike: Int,
    val perfectStrikes: Int,
    val newCharmIds: List<Int>,
    val newRecord: Boolean,
    val daily: Boolean,
    val dailyCleared: Boolean
)
