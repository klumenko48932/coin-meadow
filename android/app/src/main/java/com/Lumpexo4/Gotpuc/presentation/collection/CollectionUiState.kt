package com.Lumpexo4.Gotpuc.presentation.collection

import com.Lumpexo4.Gotpuc.domain.model.Charm

data class CharmCell(
    val charm: Charm,
    val unlocked: Boolean
)

data class CollectionUiState(
    val cells: List<CharmCell> = emptyList(),
    val unlockedCount: Int = 0,
    val totalCount: Int = 0,
    val totalCoins: Int = 0,
    val selected: CharmCell? = null
)
