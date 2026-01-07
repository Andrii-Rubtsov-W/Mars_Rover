package com.portugal1576.marsrover.domain.usecase

import com.portugal1576.marsrover.domain.model.Character
import com.portugal1576.marsrover.domain.model.Page
import com.portugal1576.marsrover.domain.repository.CharacterRepository

class GetCharactersUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(
        page: Int,
        name: String? = null,
        status: String? = null,
        species: String? = null,
        type: String? = null,
        gender: String? = null
    ): Page<Character> {
        return repository.getCharacters(
            page = page,
            name = name,
            status = status,
            species = species,
            type = type,
            gender = gender
        )
    }
}
