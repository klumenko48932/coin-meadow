package com.Lumpexo4.Gotpuc.presentation.progress

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Lumpexo4.Gotpuc.R
import com.Lumpexo4.Gotpuc.core.di.AppViewModelFactory
import com.Lumpexo4.Gotpuc.core.theme.BrandBlue
import com.Lumpexo4.Gotpuc.core.theme.BrandGold
import com.Lumpexo4.Gotpuc.core.theme.ClayWarn
import com.Lumpexo4.Gotpuc.core.theme.Hairline
import com.Lumpexo4.Gotpuc.core.theme.MeadowSage
import com.Lumpexo4.Gotpuc.core.theme.SurfaceAlt
import com.Lumpexo4.Gotpuc.core.theme.SurfaceCream
import com.Lumpexo4.Gotpuc.core.theme.TextPrimary
import com.Lumpexo4.Gotpuc.core.theme.TextSecondary
import com.Lumpexo4.Gotpuc.core.ui.ParchmentBackground
import com.Lumpexo4.Gotpuc.core.ui.ProgressBarRow
import com.Lumpexo4.Gotpuc.core.ui.ScreenHeader
import com.Lumpexo4.Gotpuc.core.ui.SecondaryButton
import com.Lumpexo4.Gotpuc.core.ui.SectionTitle
import com.Lumpexo4.Gotpuc.core.ui.StatCard
import com.Lumpexo4.Gotpuc.domain.model.formatCoins

@Composable
fun ProgressRoute(onBack: () -> Unit) {
    val viewModel: ProgressViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProgressScreen(state = state, onBack = onBack, onClaim = viewModel::claim)
}

@Composable
private fun ProgressScreen(
    state: ProgressUiState,
    onBack: () -> Unit,
    onClaim: (Int) -> Unit
) {
    ParchmentBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            ScreenHeader(titleRes = R.string.progress_title, onBack = onBack)

            if (state.player.roundsPlayed == 0) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_2),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(90.dp)
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = stringResource(R.string.progress_empty),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
                return@Column
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    top = 6.dp,
                    bottom = 28.dp
                )
            ) {
                item {
                    val level = state.level
                    if (level != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceCream),
                            border = BorderStroke(1.5.dp, Hairline),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(
                                    text = stringResource(R.string.label_level),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = stringResource(level.level.nameRes),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                ProgressBarRow(
                                    fraction = level.fraction,
                                    color = BrandBlue,
                                    caption = if (level.maxReached) {
                                        stringResource(R.string.level_max)
                                    } else {
                                        stringResource(
                                            R.string.level_progress,
                                            formatCoins(level.currentCoins),
                                            formatCoins(level.targetCoins),
                                            level.nextLevelIndex
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            value = formatCoins(state.player.totalCoins),
                            labelRes = R.string.label_total_coins,
                            valueColor = BrandGold,
                            iconRes = R.drawable.ic_coin,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            value = state.player.roundsPlayed.toString(),
                            labelRes = R.string.label_rounds,
                            valueColor = BrandBlue,
                            iconRes = R.drawable.ic_star,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            value = state.player.longestStreak.toString(),
                            labelRes = R.string.label_best_streak,
                            valueColor = MeadowSage,
                            iconRes = R.drawable.ic_bolt,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            value = state.player.perfectStrikes.toString(),
                            labelRes = R.string.label_perfect,
                            valueColor = ClayWarn,
                            iconRes = R.drawable.ic_trophy,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    SectionTitle(textRes = R.string.rewards_title)
                }

                items(state.rewards, key = { it.reward.id }) { row ->
                    RewardItem(row = row, onClaim = onClaim)
                }
            }
        }
    }
}

@Composable
private fun RewardItem(
    row: RewardRow,
    onClaim: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (row.unlocked) {
                MeadowSage.copy(alpha = 0.12f)
            } else {
                SurfaceAlt
            }
        ),
        border = BorderStroke(1.5.dp, if (row.unlocked) MeadowSage else Hairline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(
                    if (row.unlocked) R.drawable.ic_check else R.drawable.ic_lock
                ),
                contentDescription = null,
                tint = if (row.unlocked) MeadowSage else TextSecondary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(row.reward.nameRes),
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary
                )
                Text(
                    text = stringResource(
                        R.string.coins_requirement,
                        formatCoins(row.reward.coinThreshold)
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
            if (row.unlocked && !row.claimed) {
                Spacer(modifier = Modifier.width(10.dp))
                SecondaryButton(
                    textRes = R.string.claim,
                    onClick = { onClaim(row.reward.id) },
                    height = 48.dp,
                    compact = true,
                    borderColor = BrandGold,
                    modifier = Modifier.width(92.dp)
                )
            }
            if (row.claimed) {
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier.size(22.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = BrandGold,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}
