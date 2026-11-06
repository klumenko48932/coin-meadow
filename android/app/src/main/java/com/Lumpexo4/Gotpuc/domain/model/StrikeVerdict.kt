package com.Lumpexo4.Gotpuc.domain.model

enum class StrikeVerdict(val multiplier: Int) {
    Perfect(5),
    Good(3),
    Missed(1)
}
