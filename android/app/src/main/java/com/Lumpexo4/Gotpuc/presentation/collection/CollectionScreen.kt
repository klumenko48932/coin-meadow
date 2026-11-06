package com.Lumpexo4.Gotpuc.presentation.collection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Lumpexo4.Gotpuc.R
import com.Lumpexo4.Gotpuc.core.di.AppViewModelFactory
import com.Lumpexo4.Gotpuc.core.theme.BrandGold
import com.Lumpexo4.Gotpuc.core.theme.SurfaceCream
import com.Lumpexo4.Gotpuc.core.theme.TextPrimary
import com.Lumpexo4.Gotpuc.core.theme.TextSecondary
import com.Lumpexo4.Gotpuc.core.ui.CharmTile
import com.Lumpexo4.Gotpuc.core.ui.ParchmentBackground
import com.Lumpexo4.Gotpuc.core.ui.ScreenHeader
import com.Lumpexo4.Gotpuc.domain.model.formatCoins
import com.Lumpexo4.Gotpuc.presentation.dialog.CharmDialog

@Composable
fun CollectionRoute(onBack: () -> Unit) {
    val viewModel: CollectionViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    CollectionScreen(
        state = state,
        onBack = onBack,
        onSelect = viewModel::select,
        onDismiss = viewModel::dismiss
    )
}

@Composable
private fun CollectionScreen(
    state: CollectionUiState,
    onBack: () -> Unit,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    ParchmentBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            ScreenHeader(titleRes = R.string.collection_title, onBack = onBack)

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SurfaceCream,
                border = BorderStroke(1.5.dp, BrandGold),
                shadowElevation = 3.dp,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            ) {
                Text(
                    text = stringResource(
                        R.string.charms_counter,
                        state.unlockedCount,
                        state.totalCount
                    ),
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.collection_hint),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 28.dp)
            ) {
                items(state.cells, key = { it.charm.id }) { cell ->
                    CharmTile(
                        nameRes = cell.charm.nameRes,
                        artRes = charmArt(cell.charm.id),
                        unlocked = cell.unlocked,
                        costLabel = stringResource(
                            R.string.coins_requirement,
                            formatCoins(cell.charm.coinThreshold)
                        ),
                        onClick = { onSelect(cell.charm.id) }
                    )
                }
            }
        }
    }

    val selected = state.selected
    if (selected != null) {
        CharmDialog(
            nameRes = selected.charm.nameRes,
            flavorRes = selected.charm.flavorRes,
            artRes = charmArt(selected.charm.id),
            unlocked = selected.unlocked,
            costLabel = stringResource(
                R.string.coins_requirement,
                formatCoins(selected.charm.coinThreshold)
            ),
            onDismiss = onDismiss
        )
    }
}

private fun charmArt(id: Int): Int = when (id % 4) {
    0 -> R.drawable.sprite_4
    1 -> R.drawable.sprite_5
    2 -> R.drawable.sprite_1
    else -> R.drawable.sprite_2
}
