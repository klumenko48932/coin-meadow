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
import com.Lumpexo4.Gotpuc.core.theme.ClayWarn
import com.Lumpexo4.Gotpuc.core.theme.SurfaceCream
import com.Lumpexo4.Gotpuc.core.theme.TextPrimary
import com.Lumpexo4.Gotpuc.core.theme.TextSecondary
import com.Lumpexo4.Gotpuc.core.ui.SecondaryButton

@Composable
fun ConfirmResetDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        shape = RoundedCornerShape(24.dp),
        containerColor = SurfaceCream,
        title = {
            Text(
                text = stringResource(R.string.reset_confirm_title),
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = stringResource(R.string.reset_confirm_body),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
                SecondaryButton(
                    textRes = R.string.reset,
                    onClick = onConfirm,
                    borderColor = ClayWarn,
                    contentColor = ClayWarn,
                    modifier = Modifier.fillMaxWidth()
                )
                SecondaryButton(
                    textRes = R.string.cancel,
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {}
    )
}
