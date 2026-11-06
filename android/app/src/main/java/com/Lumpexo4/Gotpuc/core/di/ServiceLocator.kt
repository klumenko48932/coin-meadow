package com.Lumpexo4.Gotpuc.core.di

import android.content.Context
import com.Lumpexo4.Gotpuc.data.local.CoinMeadowPreferences
import com.Lumpexo4.Gotpuc.data.local.DayProvider
import com.Lumpexo4.Gotpuc.data.repository.CollectionRepositoryImpl
import com.Lumpexo4.Gotpuc.data.repository.ProgressRepositoryImpl
import com.Lumpexo4.Gotpuc.data.repository.SettingsRepositoryImpl
import com.Lumpexo4.Gotpuc.data.sample.SampleData
import com.Lumpexo4.Gotpuc.domain.model.Charm
import com.Lumpexo4.Gotpuc.domain.model.Reward
import com.Lumpexo4.Gotpuc.domain.repository.CollectionRepository
import com.Lumpexo4.Gotpuc.domain.repository.ProgressRepository
import com.Lumpexo4.Gotpuc.domain.repository.SettingsRepository
import com.Lumpexo4.Gotpuc.domain.usecase.GetDailyChallengeUseCase
import com.Lumpexo4.Gotpuc.domain.usecase.GetMeadowLevelUseCase
import com.Lumpexo4.Gotpuc.domain.usecase.ResetProgressUseCase
import com.Lumpexo4.Gotpuc.domain.usecase.ResolveStrikeUseCase
import com.Lumpexo4.Gotpuc.domain.usecase.ResolveTapUseCase
import com.Lumpexo4.Gotpuc.domain.usecase.SaveRoundResultUseCase
import com.Lumpexo4.Gotpuc.domain.usecase.SpawnTargetUseCase
import com.Lumpexo4.Gotpuc.domain.usecase.UnlockCharmsUseCase

object ServiceLocator {

    private var graph: Graph? = null

    fun init(context: Context) {
        if (graph == null) {
            graph = Graph(context.applicationContext)
        }
    }

    private val required: Graph
        get() = checkNotNull(graph)

    val charms: List<Charm> get() = SampleData.charms
    val rewards: List<Reward> get() = SampleData.rewards

    val progressRepository: ProgressRepository get() = required.progressRepository
    val settingsRepository: SettingsRepository get() = required.settingsRepository
    val collectionRepository: CollectionRepository get() = required.collectionRepository
    val dayProvider: DayProvider get() = required.dayProvider
    val resolveTap: ResolveTapUseCase get() = required.resolveTap
    val resolveStrike: ResolveStrikeUseCase get() = required.resolveStrike
    val spawnTarget: SpawnTargetUseCase get() = required.spawnTarget
    val saveRoundResult: SaveRoundResultUseCase get() = required.saveRoundResult
    val getDailyChallenge: GetDailyChallengeUseCase get() = required.getDailyChallenge
    val getMeadowLevel: GetMeadowLevelUseCase get() = required.getMeadowLevel
    val unlockCharms: UnlockCharmsUseCase get() = required.unlockCharms
    val resetProgress: ResetProgressUseCase get() = required.resetProgress

    private class Graph(context: Context) {
        private val preferences = CoinMeadowPreferences(context)

        val progressRepository: ProgressRepository = ProgressRepositoryImpl(preferences)
        val settingsRepository: SettingsRepository = SettingsRepositoryImpl(preferences)
        val collectionRepository: CollectionRepository = CollectionRepositoryImpl(preferences)
        val dayProvider = DayProvider()

        val resolveTap = ResolveTapUseCase()
        val resolveStrike = ResolveStrikeUseCase()
        val spawnTarget = SpawnTargetUseCase()
        val unlockCharms = UnlockCharmsUseCase(SampleData.charms)
        val getDailyChallenge = GetDailyChallengeUseCase(SampleData.dailyChallenges)
        val getMeadowLevel = GetMeadowLevelUseCase(SampleData.levels)
        val saveRoundResult = SaveRoundResultUseCase(
            progressRepository,
            collectionRepository,
            unlockCharms
        )
        val resetProgress = ResetProgressUseCase(progressRepository, collectionRepository)
    }
}
