package ru.stanise.animebrowsing.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.stanise.animebrowsing.config.Config

@Serializable
@Entity("genre")
data class Genre(
    @SerialName("mal_id")
    @PrimaryKey
    val id: Int,
    val name: String
)

fun List<Anime>.countGenres(): Map<String, Int> {
    return flatMap { it.genres }
        .groupBy { it.name }
        .entries
        .sortedByDescending { it.value.size }
        .take(Config.CHART_COLORS.size)
        .associate { it.key to it.value.size }
}