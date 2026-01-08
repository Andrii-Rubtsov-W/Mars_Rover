package com.portugal1576.marsrover.domain.usecase

import com.portugal1576.marsrover.domain.model.Episode
import com.portugal1576.marsrover.domain.repository.CharacterRepository

class GetEpisodeByIdUseCase(
    private val repo: CharacterRepository
) {
    suspend operator fun invoke(id: Int): Episode = repo.getEpisodeById(id)
}
