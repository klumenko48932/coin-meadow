package com.Lumpexo4.Gotpuc.presentation.gameover

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Lumpexo4.Gotpuc.R
import com.Lumpexo4.Gotpuc.core.di.AppViewModelFactory
import com.Lumpexo4.Gotpuc.core.navigation.Screen
import com.Lumpexo4.Gotpuc.core.theme.BrandBlue
import com.Lumpexo4.Gotpuc.core.theme.BrandGold
import com.Lumpexo4.Gotpuc.core.theme.ClayWarn
import com.Lumpexo4.Gotpuc.core.theme.MeadowSage
import com.Lumpexo4.Gotpuc.core.theme.SurfaceCream
import com.Lumpexo4.Gotpuc.core.theme.TextPrimary
import com.Lumpexo4.Gotpuc.core.theme.TextSecondary
import com.Lumpexo4.Gotpuc.core.ui.CoinChip
import com.Lumpexo4.Gotpuc.core.ui.MeadowBackground
import com.Lumpexo4.Gotpuc.core.ui.PrimaryButton
import com.Lumpexo4.Gotpuc.core.ui.SecondaryButton
import com.Lumpexo4.Gotpuc.core.ui.StatCard
import com.Lumpexo4.Gotpuc.domain.model.formatCoins

@Composable
fun GameOverRoute(
    result: Screen.GameOver,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val viewModel: GameOverViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    GameOverScreen(
        result = result,
        state = state,
        onPlayAgain = onPlayAgain,
        onMenu = onMenu
    )
}

private data class ResultStat(
    val value: String,
    val labelRes: Int,
    val color: Color
)

@Composable
private fun GameOverScreen(
    result: Screen.GameOver,
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    var revealed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        revealed = true
    }

    val badgeScale by animateFloatAsState(
        targetValue = if (revealed) 1f else 0.6f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 200f)
    )
    val headlineScale by animateFloatAsState(
        targetValue = if (revealed && result.newRecord) 1.08f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 220f)
    )
    val coins by animateIntAsState(
        targetValue = if (revealed) result.coins else 0,
        animationSpec = tween(durationMillis = 900)
    )

    val stats = buildList {
        if (result.bestStreak > 0) {
            add(
                ResultStat(
                    value = result.bestStreak.toString(),
                    labelRes = R.string.label_best_streak,
                    color = MeadowSage
                )
            )
        }
        if (result.topStrike > 1) {
            add(
                ResultStat(
                    value = "",
                    labelRes = R.string.label_top_strike,
                    color = BrandBlue
                )
            )
        }
        if (result.newCharms > 0) {
            add(
                ResultStat(
                    value = result.newCharms.toString(),
                    labelRes = R.string.label_charms,
                    color = ClayWarn
                )
            )
        }
    }
    val strikeLabel = stringResource(R.string.strike_multiplier_value, result.topStrike)

    MeadowBackground(
        overlay = listOf(
            Color(0xE6F7F0D8),
            Color(0xF2E8DDC4)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CoinChip(amount = state.totalCoins)
            }

            Spacer(modifier = Modifier.weight(0.6f))

            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(BrandGold.copy(alpha = 0.22f), Color.Transparent)
                            )
                        )
                )
                Image(
                    painter = painterResource(
                        if (result.newRecord) R.drawable.sprite_4 else R.drawable.sprite_1
                    ),
                    contentDescription = stringResource(R.string.cd_result_badge),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(96.dp)
                        .scale(badgeScale)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(
                    if (result.newRecord) R.string.result_record else R.string.result_normal
                ),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.scale(headlineScale)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = formatCoins(coins),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.ExtraBold,
                color = BrandGold,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
            Text(
                text = stringResource(R.string.coins_collected),
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            if (stats.size >= 2) {
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    stats.forEach { stat ->
                        StatCard(
                            value = if (stat.labelRes == R.string.label_top_strike) {
                                strikeLabel
                            } else {
                                stat.value
                            },
                            labelRes = stat.labelRes,
                            valueColor = stat.color,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (result.newCharms > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceCream,
                    shadowElevation = 6.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sprite_5),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(R.string.new_charms_title),
                            style = MaterialTheme.typography.labelMedium,
                            color = TextPrimary
                        )
                    }
                }
            }

            if (result.daily && result.dailyCleared) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.daily_completed),
                    style = MaterialTheme.typography.labelMedium,
                    color = MeadowSage,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                textRes = R.string.play_again,
                iconRes = R.drawable.ic_play,
                onClick = onPlayAgain,
                height = 60.dp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            SecondaryButton(
                textRes = R.string.menu,
                iconRes = R.drawable.ic_back,
                onClick = onMenu,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
