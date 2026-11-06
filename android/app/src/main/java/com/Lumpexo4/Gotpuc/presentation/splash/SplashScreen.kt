package com.Lumpexo4.Gotpuc.presentation.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
import com.Lumpexo4.Gotpuc.core.theme.MeadowSage
import com.Lumpexo4.Gotpuc.core.theme.SplashBottom
import com.Lumpexo4.Gotpuc.core.theme.SplashTop
import com.Lumpexo4.Gotpuc.core.theme.TextOnDark
import kotlin.math.abs
import kotlin.math.sin

@Composable
fun SplashRoute(onFinished: () -> Unit) {
    val viewModel: SplashViewModel = viewModel(factory = AppViewModelFactory)
    val finished by viewModel.finished.collectAsStateWithLifecycle()
    val calmMotion by viewModel.reducedMotion.collectAsStateWithLifecycle()

    LaunchedEffect(finished) {
        if (finished) onFinished()
    }

    SplashScreen(calmMotion = calmMotion)
}

@Composable
private fun SplashScreen(calmMotion: Boolean) {
    var revealed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        revealed = true
    }

    val emblemAlpha by animateFloatAsState(
        targetValue = if (revealed) 1f else 0f,
        animationSpec = tween(durationMillis = 700)
    )
    val emblemScale by animateFloatAsState(
        targetValue = if (revealed) 1f else 0.84f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 180f)
    )
    val titleAlpha by animateFloatAsState(
        targetValue = if (revealed) 1f else 0f,
        animationSpec = tween(durationMillis = 420, delayMillis = 300)
    )
    val captionAlpha by animateFloatAsState(
        targetValue = if (revealed) 1f else 0f,
        animationSpec = tween(durationMillis = 420, delayMillis = 900)
    )

    val transition = rememberInfiniteTransition()
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (calmMotion) 2600 else 5200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    val breath by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.045f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (calmMotion) 1200 else 2400),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(SplashTop, SplashBottom)))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            repeat(FIREFLY_COUNT) { index ->
                val base = fireflySeed(index)
                val x = size.width * base
                val y = size.height * (fireflySeed(index + 7) * 0.92f + 0.04f) +
                    size.height * 0.05f * (drift - 0.5f) * (if (index % 2 == 0) 1f else -1f)
                drawCircle(
                    color = BrandGold.copy(alpha = 0.22f),
                    radius = (2 + index % 4).dp.toPx(),
                    center = Offset(x, y)
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-70).dp)
                .size(320.dp)
                .background(
                    Brush.radialGradient(
                        listOf(BrandGold.copy(alpha = 0.12f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(168.dp)
                    .alpha(emblemAlpha)
                    .scale(emblemScale * breath)
            )

            Box(
                modifier = Modifier
                    .height(56.dp)
                    .alpha(titleAlpha),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.splash_title),
                    style = MaterialTheme.typography.displaySmall,
                    color = BrandGold.copy(alpha = 0.35f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.offset(x = 1.dp, y = 1.dp)
                )
                Text(
                    text = stringResource(R.string.splash_title),
                    style = MaterialTheme.typography.displaySmall,
                    color = TextOnDark,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.labelLarge,
                color = MeadowSage,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(captionAlpha)
            )

            Box(modifier = Modifier.height(40.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .width(184.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .alpha(captionAlpha),
                color = BrandGold,
                trackColor = Color.White.copy(alpha = 0.14f)
            )

            Box(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.splash_caption),
                style = MaterialTheme.typography.labelSmall,
                color = TextOnDark.copy(alpha = 0.55f),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(captionAlpha)
            )
        }
    }
}

private const val FIREFLY_COUNT = 14

private fun fireflySeed(index: Int): Float {
    val value = sin(index * 12.9898f) * 43758.5453f
    return abs(value - value.toInt())
}
