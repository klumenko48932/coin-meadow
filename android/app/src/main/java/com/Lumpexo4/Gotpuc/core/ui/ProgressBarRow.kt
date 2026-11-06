package com.Lumpexo4.Gotpuc.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.Lumpexo4.Gotpuc.core.theme.ParchmentDeep
import com.Lumpexo4.Gotpuc.core.theme.TextSecondary

@Composable
fun ProgressBarRow(
    fraction: Float,
    caption: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val animated by animateFloatAsState(
        targetValue = fraction.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 650)
    )
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(ParchmentDeep)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animated.coerceIn(0f, 1f))
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(color.copy(alpha = 0.75f), color)
                        )
                    )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = caption,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}
