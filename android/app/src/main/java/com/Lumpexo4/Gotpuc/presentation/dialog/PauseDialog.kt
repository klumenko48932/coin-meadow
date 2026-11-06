package com.Lumpexo4.Gotpuc.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Lumpexo4.Gotpuc.R
import com.Lumpexo4.Gotpuc.core.theme.SurfaceCream
import com.Lumpexo4.Gotpuc.core.theme.TextPrimary
import com.Lumpexo4.Gotpuc.core.ui.PrimaryButton
import com.Lumpexo4.Gotpuc.core.ui.SecondaryButton

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onMenu: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        shape = RoundedCornerShape(24.dp),
        containerColor = SurfaceCream,
        title = {
            Text(
                text = stringResource(R.string.paused_title),
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                PrimaryButton(
                    textRes = R.string.resume,
                    iconRes = R.drawable.ic_play,
                    onClick = onResume,
                    height = 56.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                SecondaryButton(
                    textRes = R.string.menu,
                    iconRes = R.drawable.ic_back,
                    onClick = onMenu,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {}
    )
}
