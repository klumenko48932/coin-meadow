package com.Lumpexo4.Gotpuc.domain.usecase

import com.Lumpexo4.Gotpuc.domain.model.MeadowLevel
import com.Lumpexo4.Gotpuc.domain.model.MeadowLevelProgress

class GetMeadowLevelUseCase(private val levels: List<MeadowLevel>) {

    operator fun invoke(totalCoins: Int): MeadowLevelProgress {
        val current = levels.last { totalCoins >= it.coinThreshold }
        val next = levels.firstOrNull { it.coinThreshold > totalCoins }
        if (next == null) {
            return MeadowLevelProgress(
                level = current,
                nextLevelIndex = current.index,
                currentCoins = totalCoins,
                targetCoins = current.coinThreshold,
                fraction = 1f,
                maxReached = true
            )
        }
        val span = (next.coinThreshold - current.coinThreshold).coerceAtLeast(1)
        val done = (totalCoins - current.coinThreshold).coerceAtLeast(0)
        return MeadowLevelProgress(
            level = current,
            nextLevelIndex = next.index,
            currentCoins = totalCoins,
            targetCoins = next.coinThreshold,
            fraction = (done.toFloat() / span.toFloat()).coerceIn(0f, 1f),
            maxReached = false
        )
    }
}
