package com.Lumpexo4.Gotpuc.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.Lumpexo4.Gotpuc.R
import com.Lumpexo4.Gotpuc.core.theme.Parchment
import com.Lumpexo4.Gotpuc.core.theme.ParchmentDeep

@Composable
fun ParchmentBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Parchment, ParchmentDeep)))
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_2),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 34.dp, y = 30.dp)
                .size(220.dp)
                .alpha(0.16f)
        )
        content()
    }
}
