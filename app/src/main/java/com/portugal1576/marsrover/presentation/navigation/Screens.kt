package com.portugal1576.marsrover.presentation.navigation

import android.os.Parcelable
import com.portugal1576.marsrover.data.model.PlayElement
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

sealed class Screens {
    @Parcelize
    @Serializable
    data object ListScreen : Screens(), Parcelable

    @Parcelize
    @Serializable
    data class Details(val name: PlayElement) : Screens(), Parcelable

    @Parcelize
    @Serializable
    data object Favorites : Screens(), Parcelable
}
