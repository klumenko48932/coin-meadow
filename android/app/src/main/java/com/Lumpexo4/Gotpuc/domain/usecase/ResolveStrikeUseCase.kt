package com.Lumpexo4.Gotpuc.domain.usecase

import com.Lumpexo4.Gotpuc.core.config.GameConfig
import com.Lumpexo4.Gotpuc.domain.model.StrikeVerdict
import kotlin.math.abs

class ResolveStrikeUseCase {

    operator fun invoke(needle: Float, bandFraction: Float): StrikeVerdict {
        val offset = abs(needle - 0.5f)
        return when {
            offset <= GameConfig.STRIKE_CORE_FRACTION / 2f -> StrikeVerdict.Perfect
            offset <= bandFraction / 2f -> StrikeVerdict.Good
            else -> StrikeVerdict.Missed
        }
    }
}
