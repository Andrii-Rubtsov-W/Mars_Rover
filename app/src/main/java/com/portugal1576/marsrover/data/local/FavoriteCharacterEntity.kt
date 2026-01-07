package com.portugal1576.marsrover.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.portugal1576.marsrover.domain.model.Character

@Entity(tableName = "favorite_characters")
data class FavoriteCharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val image: String,
    val status: String,
    val species: String,
    val gender: String,
    val origin: String,
    val location: String,
    val episodesCsv: String
) {
    fun toDomain(): Character {
        val episodes = if (episodesCsv.isBlank()) emptyList() else episodesCsv.split("|")
        return Character(
            id = id,
            name = name,
            image = image,
            status = status,
            species = species,
            gender = gender,
            originName = origin,
            locationName = location,
            episodeUrls = episodes,
            isFavorite = true
        )
    }

    companion object {
        fun fromDomain(character: Character): FavoriteCharacterEntity {
            return FavoriteCharacterEntity(
                id = character.id,
                name = character.name,
                image = character.image,
                status = character.status,
                species = character.species,
                gender = character.gender,
                origin = character.originName,
                location = character.locationName,
                episodesCsv = character.episodeUrls.joinToString("|")
            )
        }
    }
}
