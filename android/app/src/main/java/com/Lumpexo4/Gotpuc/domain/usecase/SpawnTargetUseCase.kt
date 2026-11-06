package com.Lumpexo4.Gotpuc.domain.usecase

import com.Lumpexo4.Gotpuc.core.config.GameConfig
import com.Lumpexo4.Gotpuc.domain.model.DailyChallenge
import com.Lumpexo4.Gotpuc.domain.model.SpawnPlan
import com.Lumpexo4.Gotpuc.domain.model.TargetKind
import kotlin.random.Random

class SpawnTargetUseCase(private val random: Random = Random.Default) {

    operator fun invoke(
        occupied: Set<Int>,
        spawnIndex: Int,
        challenge: DailyChallenge?
    ): SpawnPlan? {
        val free = (0 until GameConfig.BOARD_CELLS).filterNot { occupied.contains(it) }
        if (free.isEmpty()) return null
        val cell = free[random.nextInt(free.size)]
        return SpawnPlan(cell, pickKind(spawnIndex, challenge))
    }

    private fun pickKind(spawnIndex: Int, challenge: DailyChallenge?): TargetKind {
        if (spawnIndex < OPENING_COINS) return TargetKind.Coin
        if (challenge?.coinsOnly == true) {
            return if (random.nextInt(100) < 80) TargetKind.Coin else TargetKind.Clover
        }
        val chestWeight = CHEST_WEIGHT * (challenge?.chestWeightMultiplier ?: 1)
        val total = COIN_WEIGHT + CLOVER_WEIGHT + BEE_WEIGHT + chestWeight + CHARM_WEIGHT
        var roll = random.nextInt(total)
        if (roll < COIN_WEIGHT) return TargetKind.Coin
        roll -= COIN_WEIGHT
        if (roll < CLOVER_WEIGHT) return TargetKind.Clover
        roll -= CLOVER_WEIGHT
        if (roll < BEE_WEIGHT) return TargetKind.Bee
        roll -= BEE_WEIGHT
        if (roll < chestWeight) return TargetKind.Chest
        return TargetKind.Charm
    }

    private companion object {
        const val OPENING_COINS = 3
        const val COIN_WEIGHT = 62
        const val CLOVER_WEIGHT = 12
        const val BEE_WEIGHT = 14
        const val CHEST_WEIGHT = 8
        const val CHARM_WEIGHT = 4
    }
}
