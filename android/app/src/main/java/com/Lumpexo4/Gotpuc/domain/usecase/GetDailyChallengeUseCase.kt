package com.Lumpexo4.Gotpuc.domain.usecase

import com.Lumpexo4.Gotpuc.domain.model.DailyChallenge

class GetDailyChallengeUseCase(private val challenges: List<DailyChallenge>) {

    operator fun invoke(dayOfYear: Int): DailyChallenge =
        challenges[((dayOfYear % challenges.size) + challenges.size) % challenges.size]
}
