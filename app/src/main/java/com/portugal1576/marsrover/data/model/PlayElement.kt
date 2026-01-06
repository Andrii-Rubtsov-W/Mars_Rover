package com.portugal1576.marsrover.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class PlayElement(
    val name: String
): Parcelable