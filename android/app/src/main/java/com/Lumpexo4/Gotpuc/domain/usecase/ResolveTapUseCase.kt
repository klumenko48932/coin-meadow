package com.Lumpexo4.Gotpuc.domain.usecase

import com.Lumpexo4.Gotpuc.core.config.GameConfig
import com.Lumpexo4.Gotpuc.domain.model.TapOutcome
import com.Lumpexo4.Gotpuc.domain.model.TargetKind

class ResolveTapUseCase {

    operator fun invoke(
        kind: TargetKind,
        multiplier: Float,
        lives: Int,
        charmAvailable: Boolean
    ): TapOutcome = when (kind) {
        TargetKind.Coin -> TapOutcome(
            coinsGained = (GameConfig.COIN_VALUE * multiplier).toInt(),
            streakUp = true
        )

        TargetKind.Clover -> TapOutcome(
            timeGainedMs = GameConfig.CLOVER_TIME_BONUS_MS,
            lifeRestored = lives < GameConfig.MAX_LIVES,
            streakUp = true
        )

        TargetKind.Charm -> TapOutcome(
            coinsGained = GameConfig.CHARM_COIN_BONUS,
            charmBanked = charmAvailable,
            streakUp = true
        )

        TargetKind.Chest -> TapOutcome(opensStrike = true)

        TargetKind.Bee -> TapOutcome(lifeLost = true, streakReset = true)
    }
}
