package com.Lumpexo4.Gotpuc.domain.usecase

import com.Lumpexo4.Gotpuc.domain.repository.CollectionRepository
import com.Lumpexo4.Gotpuc.domain.repository.ProgressRepository

class ResetProgressUseCase(
    private val progressRepository: ProgressRepository,
    private val collectionRepository: CollectionRepository
) {

    operator fun invoke() {
        progressRepository.clear()
        collectionRepository.clear()
    }
}
