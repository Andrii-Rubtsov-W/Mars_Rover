package com.portugal1576.marsrover.domain.usecase

import com.portugal1576.marsrover.domain.model.Character
import com.portugal1576.marsrover.domain.repository.CharacterRepository

class GetCharacterByIdUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: Int): Character {
        return repository.getCharacterById(id)
    }
}
