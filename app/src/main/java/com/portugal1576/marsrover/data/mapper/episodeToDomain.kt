package com.portugal1576.marsrover.data.mapper

import com.portugal1576.marsrover.data.model.EpisodeDto
import com.portugal1576.marsrover.domain.model.Episode

fun EpisodeDto.toDomain(): Episode =
    Episode(
        id = id,
        name = name,
        airDate = airDate,
        code = episode
    )
