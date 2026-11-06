package com.Lumpexo4.Gotpuc.presentation.daily

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Lumpexo4.Gotpuc.R
import com.Lumpexo4.Gotpuc.core.di.AppViewModelFactory
import com.Lumpexo4.Gotpuc.core.theme.BrandBlue
import com.Lumpexo4.Gotpuc.core.theme.BrandGold
import com.Lumpexo4.Gotpuc.core.theme.Hairline
import com.Lumpexo4.Gotpuc.core.theme.MeadowSage
import com.Lumpexo4.Gotpuc.core.theme.ParchmentDeep
import com.Lumpexo4.Gotpuc.core.theme.SurfaceCream
import com.Lumpexo4.Gotpuc.core.theme.TextPrimary
import com.Lumpexo4.Gotpuc.core.theme.TextSecondary
import com.Lumpexo4.Gotpuc.core.ui.ParchmentBackground
import com.Lumpexo4.Gotpuc.core.ui.PrimaryButton
import com.Lumpexo4.Gotpuc.core.ui.ScreenHeader
import com.Lumpexo4.Gotpuc.core.ui.SecondaryButton
import com.Lumpexo4.Gotpuc.core.ui.SectionTitle

@Composable
fun DailyChallengeRoute(
    onBack: () -> Unit,
    onStart: () -> Unit
) {
    val viewModel: DailyChallengeViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    DailyChallengeScreen(state = state, onBack = onBack, onStart = onStart)
}

@Composable
private fun DailyChallengeScreen(
    state: DailyChallengeUiState,
    onBack: () -> Unit,
    onStart: () -> Unit
) {
    ParchmentBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            ScreenHeader(titleRes = R.string.daily_title, onBack = onBack)

            Spacer(modifier = Modifier.height(10.dp))

            val challenge = state.challenge
            if (challenge != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCream),
                    border = BorderStroke(2.dp, BrandGold),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(R.drawable.ic_calendar),
                                contentDescription = null,
                                tint = BrandGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = stringResource(R.string.daily_today, state.dayOfYear),
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(challenge.nameRes),
                            style = MaterialTheme.typography.headlineSmall,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(challenge.ruleRes),
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = BrandGold.copy(alpha = 0.18f),
                            border = BorderStroke(1.5.dp, BrandGold)
                        ) {
                            Text(
                                text = stringResource(challenge.targetRes),
                                style = MaterialTheme.typography.labelMedium,
                                color = TextPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle(textRes = R.string.daily_week)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.week.forEach { cell ->
                    DayPill(cell = cell, modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.daily_streak_label),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Text(
                text = state.dailyStreak.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = MeadowSage
            )

            Spacer(modifier = Modifier.weight(1f))

            if (state.completedToday) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = SurfaceCream,
                    border = BorderStroke(1.5.dp, Hairline)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.daily_completed),
                            style = MaterialTheme.typography.titleMedium,
                            color = MeadowSage,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.daily_come_back),
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(R.string.daily_next_reset),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                SecondaryButton(
                    textRes = R.string.menu,
                    iconRes = R.drawable.ic_back,
                    onClick = onBack,
                    borderColor = MeadowSage,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = stringResource(R.string.daily_reward),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                PrimaryButton(
                    textRes = R.string.start_challenge,
                    iconRes = R.drawable.ic_play,
                    onClick = onStart,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DayPill(cell: DayCell, modifier: Modifier = Modifier) {
    val fill = when (cell.state) {
        DayState.Completed -> MeadowSage
        DayState.Today -> Color.Transparent
        DayState.Future -> ParchmentDeep
        DayState.Missed -> ParchmentDeep
    }
    val border = when (cell.state) {
        DayState.Completed -> MeadowSage
        DayState.Today -> BrandBlue
        else -> Hairline
    }
    Box(
        modifier = modifier
            .height(34.dp)
            .background(fill, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.size(34.dp),
            shape = CircleShape,
            color = fill,
            border = BorderStroke(2.dp, border)
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (cell.state == DayState.Completed) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = SurfaceCream,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
