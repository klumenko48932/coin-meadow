package com.Lumpexo4.Gotpuc.domain.usecase

import com.Lumpexo4.Gotpuc.domain.model.Charm

class UnlockCharmsUseCase(private val charms: List<Charm>) {

    operator fun invoke(lifetimeCoins: Int, unlocked: Set<Int>): List<Int> =
        charms.filter { it.coinThreshold <= lifetimeCoins && !unlocked.contains(it.id) }
            .map { it.id }

    fun nextLocked(unlocked: Set<Int>): Int? =
        charms.firstOrNull { !unlocked.contains(it.id) }?.id
}
