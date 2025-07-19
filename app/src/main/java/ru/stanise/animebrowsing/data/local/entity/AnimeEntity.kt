package ru.stanise.animebrowsing.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.stanise.animebrowsing.data.local.converter.Converters
import ru.stanise.animebrowsing.dto.AnimeType
import ru.stanise.animebrowsing.dto.Season
import ru.stanise.animebrowsing.dto.Status

@Entity("anime")
@TypeConverters(Converters::class)
data class AnimeEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val image: String?,
    val type: AnimeType?,
    val status: Status?,
    val season: Season?,
    val year: Int?,
    val score: Double?,
    val duration: String?,
    val episodes: Int?,
    val synopsis: String?,
    val background: String?
)
