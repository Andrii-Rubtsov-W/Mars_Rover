package com.portugal1576.marsrover.presentation.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

sealed class Screens {
    @Parcelize
    @Serializable
    data object StartScreen : Screens(), Parcelable

    @Parcelize
    @Serializable
    data class Details(val name: String) : Screens(), Parcelable

    @Parcelize
    @Serializable
    data object Favorites : Screens(), Parcelable
}
