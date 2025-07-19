package ru.stanise.animebrowsing.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.stanise.animebrowsing.data.local.entity.AnimeEntity
import ru.stanise.animebrowsing.data.local.entity.AnimeWithGenres

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

fun AnimeWithGenres.toDto(): Anime {
    return Anime(
        id = anime.id,
        images = Images(jpg = Image(imageUrl = anime.image)),
        titles = listOf(Title(type = "English", title = anime.title)),
        type = anime.type,
        episodes = anime.episodes,
        status = anime.status,
        duration = anime.duration,
        score = anime.score,
        synopsis = anime.synopsis,
        background = anime.background,
        season = anime.season,
        year = anime.year,
        genres = genres,
        themes = emptyList(),
        demographics = emptyList()
    )
}

fun Anime.toEntityWithGenres(): Pair<AnimeEntity, List<Genre>> {
    val animeEntity = AnimeEntity(
        id = id,
        title = titles.getEnglishTitleOrFallback(),
        image = images.jpg.imageUrl,
        type = type,
        status = status,
        season = season,
        year = year,
        score = score,
        duration = duration,
        episodes = episodes,
        synopsis = synopsis,
        background = background
    )
    val genreEntities = genres + themes + demographics
    return animeEntity to genreEntities
}

