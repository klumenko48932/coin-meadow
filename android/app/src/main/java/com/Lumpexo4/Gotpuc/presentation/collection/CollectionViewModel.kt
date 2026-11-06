package com.Lumpexo4.Gotpuc.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lumpexo4.Gotpuc.domain.model.Charm
import com.Lumpexo4.Gotpuc.domain.repository.CollectionRepository
import com.Lumpexo4.Gotpuc.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionViewModel(
    progressRepository: ProgressRepository,
    collectionRepository: CollectionRepository,
    private val charms: List<Charm>
) : ViewModel() {

    private val _state = MutableStateFlow(CollectionUiState(totalCount = charms.size))
    val state: StateFlow<CollectionUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                progressRepository.progress,
                collectionRepository.unlockedCharmIds
            ) { progress, unlocked -> progress to unlocked }
                .collect { (progress, unlocked) ->
                    val cells = charms.map { charm ->
                        CharmCell(charm = charm, unlocked = unlocked.contains(charm.id))
                    }
                    _state.update {
                        it.copy(
                            cells = cells,
                            unlockedCount = cells.count { cell -> cell.unlocked },
                            totalCount = cells.size,
                            totalCoins = progress.totalCoins
                        )
                    }
                }
        }
    }

    fun select(charmId: Int) {
        _state.update { current ->
            current.copy(selected = current.cells.firstOrNull { it.charm.id == charmId })
        }
    }

    fun dismiss() {
        _state.update { it.copy(selected = null) }
    }
}
