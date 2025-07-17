package ru.stanise.animebrowsing.data.local.entity

import androidx.room.Entity


@Entity(
    tableName = "anime_genre_cross_ref",
    primaryKeys = ["animeId", "genreId"]
)
data class AnimeGenreCrossRef(
    val animeId: Int,
    val genreId: Int
)
