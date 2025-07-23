package ru.stanise.animebrowsing.dto

import kotlinx.serialization.Serializable

@Serializable(with = AnimeTypeSerializer::class)
enum class AnimeType(val rawValue: String) {
    TV("tv"),

    OVA("ova"),

    MOVIE("movie"),

    SPECIAL("special"),

    ONA("ona"),

    MUSIC("music"),

    TV_SPECIAL("tv_special"),

    UNKNOWN("unknown")
}

val allowedTypes = listOf(
    AnimeType.TV,
    AnimeType.OVA,
    AnimeType.MOVIE,
    AnimeType.SPECIAL,
    AnimeType.ONA
)