package com.portugal1576.marsrover.data.api

import com.portugal1576.marsrover.data.mapper.toDomain
import com.portugal1576.marsrover.domain.model.Character
import com.portugal1576.marsrover.domain.model.Episode
import com.portugal1576.marsrover.domain.model.Page
import com.portugal1576.marsrover.domain.repository.CharacterRepository

class RickMortyRepositoryImpl(
    private val apiService: ApiService
) : CharacterRepository {

    override suspend fun getCharacters(
        page: Int,
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ): Page<Character> {
        val response = apiService.getCharacters(
            page = page,
            name = name,
            status = status,
            species = species,
            type = type,
            gender = gender
        )

        val nextPage = if (response.info.next.isNullOrBlank()) null else page + 1

        return Page(
            items = response.results.map { it.toDomain() },
            nextPage = nextPage
        )
    }

    override suspend fun getCharacterById(id: Int): Character {
        return apiService.getCharacterById(id).toDomain()
    }

    override suspend fun getEpisodeById(id: Int): Episode {
        return apiService.getEpisodeById(id).toDomain()
    }
}
