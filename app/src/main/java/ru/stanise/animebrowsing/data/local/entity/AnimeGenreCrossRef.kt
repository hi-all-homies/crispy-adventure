package ru.stanise.animebrowsing.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import ru.stanise.animebrowsing.dto.Genre


@Entity(
    tableName = "anime_genre_cross_ref",
    primaryKeys = ["animeId", "genreId"],
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["id"],
            childColumns = ["animeId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = Genre::class,
            parentColumns = ["id"],
            childColumns = ["genreId"],
            onDelete = CASCADE
        )
    ]
)
data class AnimeGenreCrossRef(
    val animeId: Int,
    val genreId: Int
)
