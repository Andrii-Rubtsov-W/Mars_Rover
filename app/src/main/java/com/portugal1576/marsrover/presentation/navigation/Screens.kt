package com.portugal1576.marsrover.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
enum class DetailsFrom {
    START,
    FAVORITES
}

@Serializable
sealed class Screens {
    @Serializable
    data object StartScreen : Screens()

    @Serializable
    data class Details(val id: Int, val from: DetailsFrom) : Screens()

    @Serializable
    data object Favorites : Screens()
}
