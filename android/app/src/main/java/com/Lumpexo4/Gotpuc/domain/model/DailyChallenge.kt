package com.Lumpexo4.Gotpuc.domain.model

data class DailyChallenge(
    val id: Int,
    val nameRes: Int,
    val ruleRes: Int,
    val targetRes: Int,
    val durationMs: Long,
    val lives: Int,
    val coinTarget: Int,
    val strikeTarget: Int,
    val chestWeightMultiplier: Int,
    val coinsOnly: Boolean
)
