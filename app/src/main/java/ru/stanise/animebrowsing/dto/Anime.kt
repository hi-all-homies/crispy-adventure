package ru.stanise.animebrowsing.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Anime(
    @SerialName("mal_id")
    val id: Int,

    val images: Images,

    val titles: List<Title> = emptyList(),

    val type: AnimeType?,

    val episodes: Int?,

    val status: Status?,

    val duration: String?,

    val score: Double?,

    val synopsis: String?,

    val background: String?,

    val season: Season?,

    val year: Int?,

    val genres: List<Genre> = emptyList(),
    val themes: List<Genre> = emptyList(),
    val demographics: List<Genre> = emptyList()
)


@Serializable
data class Title(
    val type: String,
    val title: String
)

fun List<Title>.getEnglishTitleOrFallback(): String {
    return firstOrNull { it.type.equals("English", ignoreCase = true) }?.title
        ?: firstOrNull { it.type.equals("Default", ignoreCase = true) }?.title
        ?: firstOrNull()?.title
        ?: "Untitled"
}

fun List<Title>.getJapaneseTitleOrFallback(): String {
    return firstOrNull { it.type.equals("Japanese", ignoreCase = true) }?.title
        ?: firstOrNull { it.type.equals("Default", ignoreCase = true) }?.title
        ?: ""
}

fun Anime.getSeasonYear(): String {
    return listOfNotNull(season?.rawValue, year?.toString())
        .joinToString(" ")
}
