package com.portugal1576.marsrover.domain.model

import com.portugal1576.marsrover.data.local.FavoriteCharacterDao
import com.portugal1576.marsrover.data.local.FavoriteCharacterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class FavoriteList(
    private val dao: FavoriteCharacterDao
) {
    fun observeFavorites(): Flow<List<Character>> {
        return dao.observeAll().map { list -> list.map { it.toDomain() } }
    }

    open fun observeFavoriteIds(): Flow<Set<Int>> {
        return dao.observeIds().map { it.toSet() }
    }

    open suspend fun toggle(character: Character) {
        val existing = dao.getById(character.id)
        if (existing == null) {
            dao.upsert(FavoriteCharacterEntity.fromDomain(character))
        } else {
            dao.deleteById(character.id)
        }
    }
}
