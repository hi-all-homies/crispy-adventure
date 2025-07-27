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
    val genreCounts = flatMap { it.genres }
        .groupingBy { it.name }
        .eachCount()
        .entries
        .sortedByDescending { it.value }

    val maxGenres = Config.CHART_COLORS.size - 1
    val mainGenres = genreCounts.take(maxGenres)
    val otherGenres = genreCounts.drop(maxGenres)

    val othersCount = otherGenres.sumOf { it.value }

    val result = mainGenres.associate { it.key to it.value }.toMutableMap()
    if (othersCount > 0) {
        result["others"] = othersCount
    }
    return result
}