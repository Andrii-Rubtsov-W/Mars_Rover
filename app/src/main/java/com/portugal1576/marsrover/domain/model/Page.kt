package com.portugal1576.marsrover.domain.model

data class Page<T>(
    val items: List<T>,
    val nextPage: Int?
)
