package com.Lumpexo4.Gotpuc.presentation.progress

import com.Lumpexo4.Gotpuc.domain.model.MeadowLevelProgress
import com.Lumpexo4.Gotpuc.domain.model.PlayerProgress
import com.Lumpexo4.Gotpuc.domain.model.Reward

data class RewardRow(
    val reward: Reward,
    val unlocked: Boolean,
    val claimed: Boolean
)

data class ProgressUiState(
    val player: PlayerProgress = PlayerProgress(),
    val level: MeadowLevelProgress? = null,
    val rewards: List<RewardRow> = emptyList()
)
