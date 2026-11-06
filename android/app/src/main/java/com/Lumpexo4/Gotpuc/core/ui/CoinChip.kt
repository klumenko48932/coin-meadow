package com.Lumpexo4.Gotpuc.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Lumpexo4.Gotpuc.R
import com.Lumpexo4.Gotpuc.core.theme.BrandGold
import com.Lumpexo4.Gotpuc.core.theme.SurfaceCream
import com.Lumpexo4.Gotpuc.core.theme.TextPrimary
import com.Lumpexo4.Gotpuc.domain.model.formatCoins

@Composable
fun CoinChip(
    amount: Int,
    modifier: Modifier = Modifier,
    coinSize: Dp = 22.dp
) {
    val formatted = formatCoins(amount)
    val spoken = stringResource(R.string.cd_coin_balance, formatted)
    Surface(
        modifier = modifier.semantics {
            liveRegion = LiveRegionMode.Polite
            contentDescription = spoken
        },
        shape = RoundedCornerShape(20.dp),
        color = SurfaceCream,
        border = BorderStroke(1.5.dp, BrandGold),
        shadowElevation = 3.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_1),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(coinSize)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = formatted,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary,
                maxLines = 1
            )
        }
    }
}
