package com.Lumpexo4.Gotpuc.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Lumpexo4.Gotpuc.core.di.AppViewModelFactory
import com.Lumpexo4.Gotpuc.core.navigation.Screen
import com.Lumpexo4.Gotpuc.presentation.collection.CollectionRoute
import com.Lumpexo4.Gotpuc.presentation.daily.DailyChallengeRoute
import com.Lumpexo4.Gotpuc.presentation.game.GameRoute
import com.Lumpexo4.Gotpuc.presentation.gameover.GameOverRoute
import com.Lumpexo4.Gotpuc.presentation.menu.MenuRoute
import com.Lumpexo4.Gotpuc.presentation.progress.ProgressRoute
import com.Lumpexo4.Gotpuc.presentation.settings.SettingsRoute
import com.Lumpexo4.Gotpuc.presentation.splash.SplashRoute

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory)
    val nav by appViewModel.state.collectAsStateWithLifecycle()

    BackHandler(enabled = nav.depth > 0) { appViewModel.back() }

    AnimatedContent(
        targetState = nav.screen,
        modifier = Modifier.fillMaxSize(),
        transitionSpec = {
            fadeIn(animationSpec = tween(420)) togetherWith fadeOut(animationSpec = tween(300))
        }
    ) { screen ->
        when (screen) {
            Screen.Splash -> SplashRoute(
                onFinished = { appViewModel.replaceRoot(Screen.Menu) }
            )

            Screen.Menu -> MenuRoute(
                onPlay = { appViewModel.navigate(Screen.Game(daily = false)) },
                onDaily = { appViewModel.navigate(Screen.Daily) },
                onProgress = { appViewModel.navigate(Screen.Progress) },
                onCollection = { appViewModel.navigate(Screen.Collection) },
                onSettings = { appViewModel.navigate(Screen.Settings) }
            )

            is Screen.Game -> GameRoute(
                daily = screen.daily,
                onRoundFinished = { result ->
                    appViewModel.replaceTop(
                        Screen.GameOver(
                            coins = result.coins,
                            bestStreak = result.bestStreak,
                            topStrike = result.topStrike,
                            newCharms = result.newCharmIds.size,
                            newRecord = result.newRecord,
                            daily = result.daily,
                            dailyCleared = result.dailyCleared
                        )
                    )
                },
                onExit = { appViewModel.back() }
            )

            is Screen.GameOver -> GameOverRoute(
                result = screen,
                onPlayAgain = { appViewModel.replaceTop(Screen.Game(daily = screen.daily)) },
                onMenu = { appViewModel.back() }
            )

            Screen.Daily -> DailyChallengeRoute(
                onBack = { appViewModel.back() },
                onStart = { appViewModel.navigate(Screen.Game(daily = true)) }
            )

            Screen.Progress -> ProgressRoute(onBack = { appViewModel.back() })

            Screen.Collection -> CollectionRoute(onBack = { appViewModel.back() })

            Screen.Settings -> SettingsRoute(onBack = { appViewModel.back() })
        }
    }
}
