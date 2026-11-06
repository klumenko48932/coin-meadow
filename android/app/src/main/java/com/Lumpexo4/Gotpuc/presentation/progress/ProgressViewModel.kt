package com.Lumpexo4.Gotpuc.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lumpexo4.Gotpuc.domain.model.Reward
import com.Lumpexo4.Gotpuc.domain.repository.ProgressRepository
import com.Lumpexo4.Gotpuc.domain.usecase.GetMeadowLevelUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProgressViewModel(
    private val progressRepository: ProgressRepository,
    private val getMeadowLevel: GetMeadowLevelUseCase,
    private val rewards: List<Reward>
) : ViewModel() {

    private val _state = MutableStateFlow(ProgressUiState())
    val state: StateFlow<ProgressUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.progress.collect { player ->
                _state.update {
                    it.copy(
                        player = player,
                        level = getMeadowLevel(player.totalCoins),
                        rewards = rewards.map { reward ->
                            RewardRow(
                                reward = reward,
                                unlocked = player.totalCoins >= reward.coinThreshold,
                                claimed = player.claimedRewardIds.contains(reward.id)
                            )
                        }
                    )
                }
            }
        }
    }

    fun claim(rewardId: Int) {
        val row = _state.value.rewards.firstOrNull { it.reward.id == rewardId } ?: return
        if (!row.unlocked || row.claimed) return
        progressRepository.claimReward(rewardId, row.reward.boostCharges)
    }
}
