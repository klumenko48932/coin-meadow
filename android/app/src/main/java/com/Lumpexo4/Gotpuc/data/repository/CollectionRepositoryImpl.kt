package com.Lumpexo4.Gotpuc.data.repository

import com.Lumpexo4.Gotpuc.data.local.CoinMeadowPreferences
import com.Lumpexo4.Gotpuc.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CollectionRepositoryImpl(
    private val prefs: CoinMeadowPreferences
) : CollectionRepository {

    private val state = MutableStateFlow(prefs.ids(CoinMeadowPreferences.KEY_UNLOCKED_CHARMS))

    override val unlockedCharmIds: StateFlow<Set<Int>> = state.asStateFlow()

    override fun unlock(ids: Collection<Int>) {
        val merged = state.value + ids
        if (merged.size == state.value.size) return
        prefs.writeIds(CoinMeadowPreferences.KEY_UNLOCKED_CHARMS, merged)
        state.value = merged
    }

    override fun clear() {
        prefs.wipe(listOf(CoinMeadowPreferences.KEY_UNLOCKED_CHARMS))
        state.value = emptySet()
    }
}
