package com.Lumpexo4.Gotpuc.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface CollectionRepository {
    val unlockedCharmIds: StateFlow<Set<Int>>
    fun unlock(ids: Collection<Int>)
    fun clear()
}
