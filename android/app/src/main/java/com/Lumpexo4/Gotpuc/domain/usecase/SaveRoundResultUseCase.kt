package com.Lumpexo4.Gotpuc.domain.usecase

import com.Lumpexo4.Gotpuc.domain.model.RoundResult
import com.Lumpexo4.Gotpuc.domain.repository.CollectionRepository
import com.Lumpexo4.Gotpuc.domain.repository.ProgressRepository

class SaveRoundResultUseCase(
    private val progressRepository: ProgressRepository,
    private val collectionRepository: CollectionRepository,
    private val unlockCharms: UnlockCharmsUseCase
) {

    operator fun invoke(result: RoundResult, dayOfYear: Int) {
        progressRepository.saveRound(result, dayOfYear)
        val unlocked = collectionRepository.unlockedCharmIds.value
        val byCoins = unlockCharms(progressRepository.progress.value.totalCoins, unlocked)
        val banked = result.newCharmIds.filterNot { unlocked.contains(it) }
        val all = (banked + byCoins).distinct()
        if (all.isNotEmpty()) collectionRepository.unlock(all)
    }
}
