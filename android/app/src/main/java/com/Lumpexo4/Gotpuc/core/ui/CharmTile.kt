package com.Lumpexo4.Gotpuc.core.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Lumpexo4.Gotpuc.R
import com.Lumpexo4.Gotpuc.core.theme.BrandGold
import com.Lumpexo4.Gotpuc.core.theme.Hairline
import com.Lumpexo4.Gotpuc.core.theme.ParchmentDeep
import com.Lumpexo4.Gotpuc.core.theme.SurfaceCream
import com.Lumpexo4.Gotpuc.core.theme.TextPrimary
import com.Lumpexo4.Gotpuc.core.theme.TextSecondary

@Composable
fun CharmTile(
    @StringRes nameRes: Int,
    @DrawableRes artRes: Int,
    unlocked: Boolean,
    costLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val stateLabel = stringResource(
        if (unlocked) R.string.state_unlocked else R.string.state_locked
    )
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.82f)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .semantics { stateDescription = stateLabel },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (unlocked) SurfaceCream else ParchmentDeep
        ),
        border = BorderStroke(2.dp, if (unlocked) BrandGold else Hairline),
        elevation = CardDefaults.cardElevation(defaultElevation = if (unlocked) 6.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (unlocked) {
                    Image(
                        painter = painterResource(artRes),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(54.dp)
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_lock),
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier
                            .size(30.dp)
                            .alpha(0.45f)
                    )
                }
            }
            Text(
                text = stringResource(if (unlocked) nameRes else R.string.charm_locked),
                style = MaterialTheme.typography.labelMedium,
                color = if (unlocked) TextPrimary else TextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 2,
                modifier = Modifier.padding(top = 8.dp)
            )
            if (!unlocked) {
                Text(
                    text = costLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
        }
    }
}
