package com.Lumpexo4.Gotpuc.presentation.dialog

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Lumpexo4.Gotpuc.R
import com.Lumpexo4.Gotpuc.core.theme.SurfaceCream
import com.Lumpexo4.Gotpuc.core.theme.TextPrimary
import com.Lumpexo4.Gotpuc.core.theme.TextSecondary
import com.Lumpexo4.Gotpuc.core.ui.SecondaryButton

@Composable
fun CharmDialog(
    nameRes: Int,
    flavorRes: Int,
    @DrawableRes artRes: Int,
    unlocked: Boolean,
    costLabel: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = SurfaceCream,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(artRes),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(86.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(if (unlocked) nameRes else R.string.charm_locked),
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Text(
                text = if (unlocked) stringResource(flavorRes) else costLabel,
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            SecondaryButton(
                textRes = R.string.close,
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}
