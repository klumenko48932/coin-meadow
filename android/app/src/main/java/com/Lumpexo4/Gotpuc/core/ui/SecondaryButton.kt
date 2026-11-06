package com.Lumpexo4.Gotpuc.core.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Lumpexo4.Gotpuc.core.theme.Hairline
import com.Lumpexo4.Gotpuc.core.theme.SurfaceAlt
import com.Lumpexo4.Gotpuc.core.theme.TextPrimary

@Composable
fun SecondaryButton(
    @StringRes textRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int? = null,
    enabled: Boolean = true,
    height: Dp = 50.dp,
    compact: Boolean = false,
    borderColor: Color = Hairline,
    contentColor: Color = TextPrimary
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = 500f)
    )
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        interactionSource = interactionSource,
        contentPadding = PaddingValues(horizontal = if (compact) 4.dp else 14.dp),
        border = BorderStroke(1.5.dp, if (enabled) borderColor else Hairline),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SurfaceAlt,
            contentColor = contentColor,
            disabledContainerColor = SurfaceAlt,
            disabledContentColor = contentColor
        ),
        modifier = modifier
            .heightIn(min = 48.dp)
            .height(height)
            .scale(scale)
            .alpha(if (enabled) 1f else 0.38f)
    ) {
        if (iconRes != null) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = stringResource(textRes),
            style = if (compact) {
                MaterialTheme.typography.labelSmall
            } else {
                MaterialTheme.typography.titleSmall
            },
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
