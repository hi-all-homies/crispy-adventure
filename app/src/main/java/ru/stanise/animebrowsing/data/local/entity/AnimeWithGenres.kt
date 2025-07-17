package ru.stanise.animebrowsing.data.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.stanise.animebrowsing.dto.Genre

data class AnimeWithGenres(
    @Embedded val anime: AnimeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = AnimeGenreCrossRef::class,
            parentColumn = "animeId",
            entityColumn = "genreId"
        )
    )
    val genres: List<Genre>
)
