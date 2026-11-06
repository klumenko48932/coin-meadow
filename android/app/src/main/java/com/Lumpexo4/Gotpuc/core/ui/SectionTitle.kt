package com.Lumpexo4.Gotpuc.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Lumpexo4.Gotpuc.core.theme.BrandGold
import com.Lumpexo4.Gotpuc.core.theme.TextSecondary

@Composable
fun SectionTitle(
    @StringRes textRes: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.labelMedium,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            modifier = Modifier
                .width(24.dp)
                .height(2.dp)
                .background(BrandGold, RoundedCornerShape(1.dp))
        )
    }
}
