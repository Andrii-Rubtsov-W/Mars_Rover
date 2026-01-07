package com.portugal1576.marsrover.domain.repository

import com.portugal1576.marsrover.domain.model.Character
import com.portugal1576.marsrover.domain.model.Page

interface CharacterRepository {
    suspend fun getCharacters(
        page: Int,
        name: String? = null,
        status: String? = null,
        species: String? = null,
        type: String? = null,
        gender: String? = null
    ): Page<Character>

    suspend fun getCharacterById(id: Int): Character
}
