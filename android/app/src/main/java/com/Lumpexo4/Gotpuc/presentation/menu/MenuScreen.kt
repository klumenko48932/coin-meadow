package com.Lumpexo4.Gotpuc.presentation.menu

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Lumpexo4.Gotpuc.R
import com.Lumpexo4.Gotpuc.core.di.AppViewModelFactory
import com.Lumpexo4.Gotpuc.core.theme.BrandGold
import com.Lumpexo4.Gotpuc.core.theme.ClayWarn
import com.Lumpexo4.Gotpuc.core.theme.Hairline
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
fun MenuRoute(
    onPlay: () -> Unit,
    onDaily: () -> Unit,
    onProgress: () -> Unit,
    onCollection: () -> Unit,
    onSettings: () -> Unit
) {
    val viewModel: MenuViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    MenuScreen(
        state = state,
        onPlay = onPlay,
        onDaily = onDaily,
        onProgress = onProgress,
        onCollection = onCollection,
        onSettings = onSettings
    )
}

@Composable
private fun MenuScreen(
    state: MenuUiState,
    onPlay: () -> Unit,
    onDaily: () -> Unit,
    onProgress: () -> Unit,
    onCollection: () -> Unit,
    onSettings: () -> Unit
) {
    var revealed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        revealed = true
    }

    val sheetOffset by animateDpAsState(
        targetValue = if (revealed) 0.dp else 56.dp,
        animationSpec = spring(dampingRatio = 0.78f, stiffness = 260f)
    )
    val ctaScale by animateFloatAsState(
        targetValue = if (revealed) 1f else 0.94f,
        animationSpec = tween(durationMillis = 240, delayMillis = 240)
    )

    val transition = rememberInfiniteTransition()
    val bob by transition.animateFloat(
        initialValue = 0f,
        targetValue = if (state.reducedMotion) 0f else -9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1700),
            repeatMode = RepeatMode.Reverse
        )
    )

    MeadowBackground(
        overlay = listOf(
            Color(0x26000000),
            Color(0x00000000),
            Color(0xCCF7F0D8)
        ),
        describeArt = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CoinChip(amount = state.totalCoins)
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    shape = CircleShape,
                    color = SurfaceCream,
                    border = BorderStroke(1.5.dp, BrandGold),
                    shadowElevation = 3.dp
                ) {
                    IconButton(
                        onClick = onSettings,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_settings),
                            contentDescription = stringResource(R.string.cd_settings),
                            tint = TextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = (-18).dp)
                        .size(120.dp)
                )
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 26.dp, top = 12.dp)
                        .offset(y = bob.dp)
                        .size(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(300.dp))
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .offset(y = sheetOffset),
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            color = SurfaceCream,
            shadowElevation = 18.dp,
            border = BorderStroke(2.dp, Hairline)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp)
                    .padding(top = 12.dp, bottom = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(5.dp)
                        .background(Hairline, RoundedCornerShape(3.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.menu_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.menu_tagline),
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (state.bestScore > 0 && state.longestStreak > 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            value = formatCoins(state.bestScore),
                            labelRes = R.string.label_best,
                            valueColor = BrandGold,
                            iconRes = R.drawable.ic_trophy,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            value = state.longestStreak.toString(),
                            labelRes = R.string.label_streak,
                            valueColor = MeadowSage,
                            iconRes = R.drawable.ic_bolt,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                PrimaryButton(
                    textRes = R.string.play,
                    iconRes = R.drawable.ic_play,
                    onClick = onPlay,
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(ctaScale)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        SecondaryButton(
                            textRes = R.string.daily,
                            compact = true,
                            onClick = onDaily,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (state.dailyPending) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(end = 8.dp, top = 8.dp)
                                    .size(10.dp)
                                    .background(ClayWarn, CircleShape)
                            )
                        }
                    }
                    SecondaryButton(
                        textRes = R.string.progress,
                        compact = true,
                        onClick = onProgress,
                        modifier = Modifier.weight(1f)
                    )
                    SecondaryButton(
                        textRes = R.string.collection,
                        compact = true,
                        onClick = onCollection,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
