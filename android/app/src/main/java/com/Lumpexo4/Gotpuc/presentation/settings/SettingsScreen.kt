package com.Lumpexo4.Gotpuc.presentation.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Lumpexo4.Gotpuc.R
import com.Lumpexo4.Gotpuc.core.di.AppViewModelFactory
import com.Lumpexo4.Gotpuc.core.theme.BrandGold
import com.Lumpexo4.Gotpuc.core.theme.ClayWarn
import com.Lumpexo4.Gotpuc.core.theme.Hairline
import com.Lumpexo4.Gotpuc.core.theme.MeadowSage
import com.Lumpexo4.Gotpuc.core.theme.ParchmentDeep
import com.Lumpexo4.Gotpuc.core.theme.SurfaceCream
import com.Lumpexo4.Gotpuc.core.theme.TextPrimary
import com.Lumpexo4.Gotpuc.core.theme.TextSecondary
import com.Lumpexo4.Gotpuc.core.ui.ParchmentBackground
import com.Lumpexo4.Gotpuc.core.ui.ScreenHeader
import com.Lumpexo4.Gotpuc.core.ui.SecondaryButton
import com.Lumpexo4.Gotpuc.core.ui.SectionTitle
import com.Lumpexo4.Gotpuc.domain.model.Difficulty
import com.Lumpexo4.Gotpuc.presentation.dialog.ConfirmResetDialog

@Composable
fun SettingsRoute(onBack: () -> Unit) {
    val viewModel: SettingsViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsScreen(
        state = state,
        onBack = onBack,
        onSound = viewModel::setSound,
        onHaptics = viewModel::setHaptics,
        onReducedMotion = viewModel::setReducedMotion,
        onDifficulty = viewModel::setDifficulty,
        onAskReset = viewModel::askReset,
        onCancelReset = viewModel::cancelReset,
        onConfirmReset = viewModel::confirmReset
    )
}

@Composable
private fun SettingsScreen(
    state: SettingsUiState,
    onBack: () -> Unit,
    onSound: (Boolean) -> Unit,
    onHaptics: (Boolean) -> Unit,
    onReducedMotion: (Boolean) -> Unit,
    onDifficulty: (Difficulty) -> Unit,
    onAskReset: () -> Unit,
    onCancelReset: () -> Unit,
    onConfirmReset: () -> Unit
) {
    ParchmentBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            ScreenHeader(titleRes = R.string.settings, onBack = onBack)

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCream),
                border = BorderStroke(1.5.dp, Hairline),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)) {
                    ToggleRow(
                        labelRes = R.string.settings_sound,
                        iconRes = if (state.settings.soundEnabled) {
                            R.drawable.ic_sound_on
                        } else {
                            R.drawable.ic_sound_off
                        },
                        checked = state.settings.soundEnabled,
                        onCheckedChange = onSound
                    )
                    HorizontalDivider(color = ParchmentDeep)
                    ToggleRow(
                        labelRes = R.string.settings_haptics,
                        iconRes = R.drawable.ic_bolt,
                        checked = state.settings.hapticsEnabled,
                        onCheckedChange = onHaptics
                    )
                    HorizontalDivider(color = ParchmentDeep)
                    ToggleRow(
                        labelRes = R.string.settings_reduced_motion,
                        iconRes = R.drawable.ic_star,
                        checked = state.settings.reducedMotion,
                        onCheckedChange = onReducedMotion
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle(textRes = R.string.settings_difficulty)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DifficultyChoice(
                    labelRes = R.string.difficulty_calm,
                    selected = state.settings.difficulty == Difficulty.Calm,
                    onClick = { onDifficulty(Difficulty.Calm) },
                    modifier = Modifier.weight(1f)
                )
                DifficultyChoice(
                    labelRes = R.string.difficulty_meadow,
                    selected = state.settings.difficulty == Difficulty.Meadow,
                    onClick = { onDifficulty(Difficulty.Meadow) },
                    modifier = Modifier.weight(1f)
                )
                DifficultyChoice(
                    labelRes = R.string.difficulty_strike,
                    selected = state.settings.difficulty == Difficulty.Strike,
                    onClick = { onDifficulty(Difficulty.Strike) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            SecondaryButton(
                textRes = R.string.reset_progress,
                iconRes = R.drawable.ic_lock,
                onClick = onAskReset,
                borderColor = ClayWarn,
                contentColor = ClayWarn,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = stringResource(R.string.settings_footer),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.settings_footer_note),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }

    if (state.confirmingReset) {
        ConfirmResetDialog(onConfirm = onConfirmReset, onCancel = onCancelReset)
    }
}

@Composable
private fun ToggleRow(
    @StringRes labelRes: Int,
    @DrawableRes iconRes: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val spoken = stringResource(if (checked) R.string.state_on else R.string.state_off)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .semantics { stateDescription = spoken },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = if (checked) MeadowSage else TextSecondary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.titleSmall,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = SurfaceCream,
                checkedTrackColor = MeadowSage,
                uncheckedThumbColor = SurfaceCream,
                uncheckedTrackColor = Hairline
            )
        )
    }
}

@Composable
private fun DifficultyChoice(
    @StringRes labelRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        SecondaryButton(
            textRes = labelRes,
            onClick = onClick,
            compact = true,
            borderColor = if (selected) BrandGold else Hairline,
            contentColor = if (selected) TextPrimary else TextSecondary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
