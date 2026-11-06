package com.Lumpexo4.Gotpuc.domain.model

enum class Difficulty(val lifetimeBonusMs: Long, val bandBonus: Float) {
    Calm(400L, 0.06f),
    Meadow(0L, 0f),
    Strike(-220L, -0.04f)
}
