package com.portugal1576.marsrover.presentation.screens.start_screen

enum class SortKey { NONE, STATUS, SPECIES, GENDER }

data class SortConfig(
    val key: SortKey = SortKey.NONE,
    val ascending: Boolean = true
)
