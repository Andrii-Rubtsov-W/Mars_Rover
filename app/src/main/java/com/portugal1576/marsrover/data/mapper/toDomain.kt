package com.portugal1576.marsrover.data.mapper

import com.portugal1576.marsrover.data.model.ResultDto
import com.portugal1576.marsrover.domain.model.Character

fun ResultDto.toDomain(): Character {
    return Character(
        id = id,
        name = name,
        image = image,
        status = status,
        species = species,
        gender = gender,
        originName = origin.name,
        locationName = location.name,
        episodeUrls = episode,
        isFavorite = false
    )
}
