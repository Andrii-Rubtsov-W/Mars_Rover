package com.portugal1576.marsrover.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Character(
    val id: Int,
    val name: String,
    val image: String,
    val status: String,
    val species: String,
    val gender: String,
    val originName: String,
    val locationName: String,
    val episodeUrls: List<String>,
    val isFavorite: Boolean = false
): Parcelable
