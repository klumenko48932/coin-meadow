package com.Lumpexo4.Gotpuc.presentation.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Lumpexo4.Gotpuc.R
import com.Lumpexo4.Gotpuc.core.config.GameConfig
import com.Lumpexo4.Gotpuc.core.di.AppViewModelFactory
import com.Lumpexo4.Gotpuc.core.theme.BoardDeep
import com.Lumpexo4.Gotpuc.core.theme.BrandBlue
import com.Lumpexo4.Gotpuc.core.theme.BrandGold
import com.Lumpexo4.Gotpuc.core.theme.ClayWarn
import com.Lumpexo4.Gotpuc.core.theme.Hairline
import com.Lumpexo4.Gotpuc.core.theme.MeadowDeep
import com.Lumpexo4.Gotpuc.core.theme.MeadowSage
import com.Lumpexo4.Gotpuc.core.theme.SurfaceCream
import com.Lumpexo4.Gotpuc.core.theme.TextPrimary
import com.Lumpexo4.Gotpuc.core.theme.TextSecondary
import com.Lumpexo4.Gotpuc.core.ui.CoinChip
import com.Lumpexo4.Gotpuc.core.ui.MeadowBackground
import com.Lumpexo4.Gotpuc.domain.model.RoundResult
import com.Lumpexo4.Gotpuc.domain.model.StrikeVerdict
import com.Lumpexo4.Gotpuc.domain.model.Target
import com.Lumpexo4.Gotpuc.domain.model.TargetKind
import com.Lumpexo4.Gotpuc.domain.model.formatMultiplier
import com.Lumpexo4.Gotpuc.presentation.dialog.PauseDialog
import kotlin.math.floor

@Composable
fun GameRoute(
    daily: Boolean,
    onRoundFinished: (RoundResult) -> Unit,
    onExit: () -> Unit
) {
    val viewModel: GameViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(daily) { viewModel.startRound(daily) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { onRoundFinished(it) }
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.pause() }
    }

    GameScreen(
        state = state,
        onTargetTap = viewModel::onTargetTap,
        onStrikeTap = viewModel::onStrikeTap,
        onBoost = viewModel::useBoost,
        onPause = viewModel::pause,
        onResume = viewModel::resume,
        onMenu = onExit
    )
}

@Composable
private fun GameScreen(
    state: GameUiState,
    onTargetTap: (Long) -> Unit,
    onStrikeTap: (Float) -> Unit,
    onBoost: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onMenu: () -> Unit
) {
    val shake = remember { Animatable(0f) }
    LaunchedEffect(state.shakeTick) {
        if (state.shakeTick > 0) {
            repeat(3) {
                shake.animateTo(6f, tween(durationMillis = 40))
                shake.animateTo(-6f, tween(durationMillis = 40))
            }
            shake.animateTo(0f, tween(durationMillis = 40))
        }
    }

    MeadowBackground(
        overlay = listOf(
            Color(0x33FFFFFF),
            Color(0x00000000),
            Color(0x55173A1E)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(
                state = state,
                onPause = onPause
            )

            LifePips(lives = state.lives, maxLives = state.maxLives)

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                val frame = 10.dp
                val gap = 8.dp
                val widest = if (maxWidth < 380.dp) maxWidth else 380.dp
                val tileByWidth = (widest - frame * 2 - gap * (COLS - 1)) / COLS
                val tileByHeight = (maxHeight - frame * 2 - gap * (ROWS - 1)) / ROWS
                val rawTile = if (tileByWidth < tileByHeight) tileByWidth else tileByHeight
                val tile = floor(rawTile.value).coerceAtLeast(28f).dp
                val boardWidth = tile * COLS + gap * (COLS - 1) + frame * 2

                Surface(
                    modifier = Modifier
                        .width(boardWidth)
                        .offset(x = shake.value.dp),
                    shape = RoundedCornerShape(22.dp),
                    color = BoardDeep.copy(alpha = 0.26f),
                    border = BorderStroke(2.dp, MeadowSage.copy(alpha = 0.55f))
                ) {
                    Column(
                        modifier = Modifier.padding(frame),
                        verticalArrangement = Arrangement.spacedBy(gap)
                    ) {
                        repeat(ROWS) { row ->
                            Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                                repeat(COLS) { column ->
                                    val cell = row * COLS + column
                                    MeadowPatch(
                                        tile = tile,
                                        target = state.targets.firstOrNull { it.cell == cell },
                                        burst = state.burst?.takeIf { it.cell == cell },
                                        onTargetTap = onTargetTap
                                    )
                                }
                            }
                        }
                    }
                }

                if (state.streak >= GameConfig.STREAK_RIBBON_MIN) {
                    StreakRibbon(
                        multiplier = state.multiplier,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 6.dp)
                    )
                }

                if (state.levelBanner > 0) {
                    LevelBanner(
                        level = state.levelBanner,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }

                if (state.phase == GamePhase.Idle) {
                    Text(
                        text = stringResource(R.string.game_hint),
                        style = MaterialTheme.typography.titleMedium,
                        color = SurfaceCream,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .alpha(0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(110.dp))
        }

        FloatingHud(
            state = state,
            onBoost = onBoost,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 26.dp)
        )

        if (state.phase == GamePhase.Strike) {
            StrikeOverlay(state = state, onStrikeTap = onStrikeTap)
        }
    }

    if (state.phase == GamePhase.Paused) {
        PauseDialog(onResume = onResume, onMenu = onMenu)
    }
}

@Composable
private fun GameHeader(
    state: GameUiState,
    onPause: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(R.drawable.icon_pause),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(44.dp)
            )
            IconButton(onClick = onPause, modifier = Modifier.size(48.dp)) {
                Icon(
                    painter = painterResource(R.drawable.ic_pause),
                    contentDescription = stringResource(R.string.cd_pause),
                    tint = TextPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        TimerRing(
            fraction = state.timeFraction,
            seconds = state.secondsLeft
        )

        Spacer(modifier = Modifier.weight(1f))

        CoinChip(amount = state.coins, coinSize = 20.dp)
    }
}

@Composable
private fun TimerRing(fraction: Float, seconds: Int) {
    val sweepColor = if (seconds <= 10) ClayWarn else BrandBlue
    val spoken = stringResource(R.string.cd_time_left, seconds)
    Box(
        modifier = Modifier
            .size(52.dp)
            .semantics {
                liveRegion = LiveRegionMode.Polite
                contentDescription = spoken
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 5.dp.toPx()
            val inset = stroke / 2f
            val arcSize = Size(size.width - stroke, size.height - stroke)
            drawArc(
                color = Color.White.copy(alpha = 0.28f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = androidx.compose.ui.geometry.Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = stroke)
            )
            drawArc(
                color = sweepColor,
                startAngle = -90f,
                sweepAngle = 360f * fraction,
                useCenter = false,
                topLeft = androidx.compose.ui.geometry.Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = stroke)
            )
        }
        Text(
            text = stringResource(R.string.seconds_left, seconds),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = TextPrimary
        )
    }
}

@Composable
private fun LifePips(lives: Int, maxLives: Int) {
    val spoken = stringResource(R.string.cd_lives, lives)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .semantics {
                liveRegion = LiveRegionMode.Polite
                contentDescription = spoken
            },
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(maxLives) { index ->
            Image(
                painter = painterResource(R.drawable.sprite_2),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(16.dp)
                    .alpha(if (index < lives) 1f else 0.22f)
            )
        }
    }
}

@Composable
private fun MeadowPatch(
    tile: Dp,
    target: Target?,
    burst: HitBurst?,
    onTargetTap: (Long) -> Unit
) {
    Box(
        modifier = Modifier
            .size(tile)
            .clip(RoundedCornerShape(16.dp))
            .background(MeadowDeep.copy(alpha = 0.30f))
            .background(
                Brush.verticalGradient(
                    listOf(Color.Transparent, Color.Black.copy(alpha = 0.18f))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (target != null) {
            TargetSprite(target = target, tile = tile, onTargetTap = onTargetTap)
        }
        if (burst != null) {
            HitFeedback(burst = burst)
        }
    }
}

@Composable
private fun TargetSprite(
    target: Target,
    tile: Dp,
    onTargetTap: (Long) -> Unit
) {
    var shown by remember(target.id) { mutableStateOf(false) }
    LaunchedEffect(target.id) { shown = true }
    val scale by animateFloatAsState(
        targetValue = if (shown) 1f else 0.3f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 420f)
    )
    val fade by animateFloatAsState(
        targetValue = if (shown) 1f else 0f,
        animationSpec = tween(durationMillis = 140)
    )
    Image(
        painter = painterResource(spriteFor(target.kind)),
        contentDescription = stringResource(descriptionFor(target.kind)),
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(tile * 0.78f)
            .scale(scale)
            .alpha(fade)
            .clickable { onTargetTap(target.id) }
    )
}

@Composable
private fun HitFeedback(burst: HitBurst) {
    var started by remember(burst.id) { mutableStateOf(false) }
    LaunchedEffect(burst.id) { started = true }
    val rise by animateFloatAsState(
        targetValue = if (started) -34f else 0f,
        animationSpec = tween(durationMillis = 520)
    )
    val fade by animateFloatAsState(
        targetValue = if (started) 0f else 1f,
        animationSpec = tween(durationMillis = 520)
    )
    val ring by animateFloatAsState(
        targetValue = if (started) 1.6f else 0.4f,
        animationSpec = tween(durationMillis = 320)
    )
    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .scale(ring)
                .alpha(fade * 0.9f)
                .background(
                    Brush.radialGradient(
                        listOf(Color.Transparent, BrandGold.copy(alpha = 0.55f))
                    ),
                    CircleShape
                )
        )
        if (burst.sparkle) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .scale(ring)
                    .alpha(fade)
                    .background(BrandGold.copy(alpha = 0.35f), CircleShape)
            )
        }
        Text(
            text = stringResource(R.string.coin_gain, burst.amount),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.ExtraBold,
            color = BrandGold,
            modifier = Modifier
                .offset(y = rise.dp)
                .alpha(fade)
        )
    }
}

@Composable
private fun StreakRibbon(multiplier: Float, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = BrandGold,
        shadowElevation = 6.dp
    ) {
        Text(
            text = stringResource(R.string.streak_ribbon, formatMultiplier(multiplier)),
            style = MaterialTheme.typography.labelMedium,
            color = TextPrimary,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun LevelBanner(level: Int, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = SurfaceCream.copy(alpha = 0.94f),
        border = BorderStroke(2.dp, BrandGold),
        shadowElevation = 10.dp
    ) {
        Text(
            text = stringResource(R.string.level_banner, level),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = TextPrimary,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
        )
    }
}

@Composable
private fun FloatingHud(
    state: GameUiState,
    onBoost: () -> Unit,
    modifier: Modifier = Modifier
) {
    val boostReady = state.boostCharges > 0 && state.phase == GamePhase.Playing
    val boostLabel = stringResource(
        when {
            state.boostActive -> R.string.boost_on
            boostReady -> R.string.boost_off
            else -> R.string.boost_empty
        }
    )
    Surface(
        modifier = modifier.fillMaxWidth(0.92f),
        shape = RoundedCornerShape(24.dp),
        color = SurfaceCream.copy(alpha = 0.92f),
        border = BorderStroke(2.dp, Hairline),
        shadowElevation = 14.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MiniStat(
                iconRes = R.drawable.ic_bolt,
                value = stringResource(
                    R.string.multiplier_value,
                    formatMultiplier(state.multiplier)
                ),
                labelRes = R.string.label_multiplier,
                tint = BrandBlue
            )
            MiniStat(
                iconRes = R.drawable.ic_star,
                value = state.level.toString(),
                labelRes = R.string.label_level,
                tint = MeadowSage
            )
            Surface(
                shape = CircleShape,
                color = if (boostReady) BrandGold else Hairline,
                border = BorderStroke(2.dp, if (boostReady) BrandGold else Hairline),
                modifier = Modifier
                    .size(52.dp)
                    .alpha(if (boostReady) 1f else 0.32f)
                    .semantics { stateDescription = boostLabel }
            ) {
                IconButton(
                    onClick = onBoost,
                    enabled = boostReady,
                    modifier = Modifier.size(52.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_bolt),
                        contentDescription = stringResource(R.string.cd_boost),
                        tint = TextPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MiniStat(
    iconRes: Int,
    value: String,
    labelRes: Int,
    tint: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = tint
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}

@Composable
private fun StrikeOverlay(
    state: GameUiState,
    onStrikeTap: (Float) -> Unit
) {
    val sweep = rememberInfiniteTransition()
    val live by sweep.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = GameConfig.STRIKE_SWEEP_MS,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    val verdict = state.strikeVerdict
    val frozen = verdict != null
    val needle = if (frozen) state.strikeNeedle else live
    val verdictScale by animateFloatAsState(
        targetValue = if (frozen) 1f else 1.3f,
        animationSpec = tween(durationMillis = 260)
    )
    val potValue by animateIntAsState(
        targetValue = state.strikePot,
        animationSpec = tween(durationMillis = 260)
    )
    val description = stringResource(R.string.cd_strike_meter)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.45f))
            .clickable(enabled = !frozen) { onStrikeTap(needle) }
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .width(300.dp)
                .padding(horizontal = 0.dp),
            shape = RoundedCornerShape(20.dp),
            color = SurfaceCream,
            border = BorderStroke(2.dp, BrandGold),
            shadowElevation = 16.dp
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.strike_title),
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                ) {
                    val trackWidth = maxWidth
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MeadowDeep)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .width(trackWidth * state.strikeBand)
                            .height(34.dp)
                            .background(BrandGold.copy(alpha = 0.85f))
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .width(trackWidth * GameConfig.STRIKE_CORE_FRACTION)
                            .height(34.dp)
                            .background(BrandBlue)
                    )
                    Box(
                        modifier = Modifier
                            .offset(x = (trackWidth - 6.dp) * needle)
                            .width(6.dp)
                            .height(34.dp)
                            .background(SurfaceCream)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(
                        R.string.strike_pot,
                        potValue.toString()
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
                if (verdict != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(verdictTextFor(verdict)),
                        style = MaterialTheme.typography.headlineSmall,
                        color = verdictColorFor(verdict),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.scale(verdictScale)
                    )
                }
            }
        }
    }
}

private const val COLS = GameConfig.BOARD_COLS
private const val ROWS = GameConfig.BOARD_ROWS

private fun spriteFor(kind: TargetKind): Int = when (kind) {
    TargetKind.Coin -> R.drawable.sprite_1
    TargetKind.Clover -> R.drawable.sprite_2
    TargetKind.Bee -> R.drawable.sprite_3
    TargetKind.Chest -> R.drawable.sprite_4
    TargetKind.Charm -> R.drawable.sprite_5
}

private fun descriptionFor(kind: TargetKind): Int = when (kind) {
    TargetKind.Coin -> R.string.cd_target_coin
    TargetKind.Clover -> R.string.cd_target_clover
    TargetKind.Bee -> R.string.cd_target_bee
    TargetKind.Chest -> R.string.cd_target_chest
    TargetKind.Charm -> R.string.cd_target_charm
}

private fun verdictTextFor(verdict: StrikeVerdict): Int = when (verdict) {
    StrikeVerdict.Perfect -> R.string.strike_perfect
    StrikeVerdict.Good -> R.string.strike_good
    StrikeVerdict.Missed -> R.string.strike_missed
}

private fun verdictColorFor(verdict: StrikeVerdict): Color = when (verdict) {
    StrikeVerdict.Perfect -> BrandBlue
    StrikeVerdict.Good -> BrandGold
    StrikeVerdict.Missed -> ClayWarn
}
