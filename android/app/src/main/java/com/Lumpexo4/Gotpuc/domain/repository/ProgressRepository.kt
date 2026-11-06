package com.Lumpexo4.Gotpuc.domain.repository

import com.Lumpexo4.Gotpuc.domain.model.PlayerProgress
import com.Lumpexo4.Gotpuc.domain.model.RoundResult
import kotlinx.coroutines.flow.StateFlow

interface ProgressRepository {
    val progress: StateFlow<PlayerProgress>
    fun saveRound(result: RoundResult, dayOfYear: Int)
    fun claimReward(rewardId: Int, boostCharges: Int)
    fun spendBoostCharge()
    fun clear()
}
