package com.Lumpexo4.Gotpuc.core.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.Lumpexo4.Gotpuc.presentation.app.AppViewModel
import com.Lumpexo4.Gotpuc.presentation.collection.CollectionViewModel
import com.Lumpexo4.Gotpuc.presentation.daily.DailyChallengeViewModel
import com.Lumpexo4.Gotpuc.presentation.game.GameViewModel
import com.Lumpexo4.Gotpuc.presentation.gameover.GameOverViewModel
import com.Lumpexo4.Gotpuc.presentation.menu.MenuViewModel
import com.Lumpexo4.Gotpuc.presentation.progress.ProgressViewModel
import com.Lumpexo4.Gotpuc.presentation.settings.SettingsViewModel
import com.Lumpexo4.Gotpuc.presentation.splash.SplashViewModel

val AppViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
    initializer { AppViewModel() }
    initializer { SplashViewModel(ServiceLocator.settingsRepository) }
    initializer {
        MenuViewModel(
            ServiceLocator.progressRepository,
            ServiceLocator.settingsRepository,
            ServiceLocator.dayProvider
        )
    }
    initializer {
        GameViewModel(
            ServiceLocator.progressRepository,
            ServiceLocator.settingsRepository,
            ServiceLocator.collectionRepository,
            ServiceLocator.resolveTap,
            ServiceLocator.resolveStrike,
            ServiceLocator.spawnTarget,
            ServiceLocator.saveRoundResult,
            ServiceLocator.unlockCharms,
            ServiceLocator.getDailyChallenge,
            ServiceLocator.dayProvider
        )
    }
    initializer {
        GameOverViewModel(
            ServiceLocator.progressRepository,
            ServiceLocator.settingsRepository
        )
    }
    initializer {
        DailyChallengeViewModel(
            ServiceLocator.progressRepository,
            ServiceLocator.getDailyChallenge,
            ServiceLocator.dayProvider
        )
    }
    initializer {
        ProgressViewModel(
            ServiceLocator.progressRepository,
            ServiceLocator.getMeadowLevel,
            ServiceLocator.rewards
        )
    }
    initializer {
        CollectionViewModel(
            ServiceLocator.progressRepository,
            ServiceLocator.collectionRepository,
            ServiceLocator.charms
        )
    }
    initializer {
        SettingsViewModel(
            ServiceLocator.settingsRepository,
            ServiceLocator.resetProgress
        )
    }
}
