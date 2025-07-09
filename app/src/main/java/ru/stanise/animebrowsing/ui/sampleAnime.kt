package ru.stanise.animebrowsing.ui

import ru.stanise.animebrowsing.AnimeListQuery
import ru.stanise.animebrowsing.type.AnimeKindEnum
import ru.stanise.animebrowsing.type.AnimeRatingEnum
import ru.stanise.animebrowsing.type.AnimeStatusEnum

val sampleAnime = AnimeListQuery.Anime(
    airedOn = AnimeListQuery.AiredOn(day = 1, month = 10, year = 2023),
    characterRoles = listOf(
        AnimeListQuery.CharacterRole(
            id = "1",
            rolesEn = listOf("Main"),
            character = AnimeListQuery.Character(
                id = "c1",
                name = "Frieren",
                russian = "Фрирен",
                description = "An elven mage who was part of the hero's party.",
                poster = AnimeListQuery.Poster(
                    mainUrl = "https://example.com/character.jpg",
                    mainAltUrl = "https://example.com/character_alt.jpg"
                )
            )
        )
    ),
    description = "A touching journey of an elf reflecting on life and loss after the war is over.",
    duration = 24,
    english = "Frieren: Beyond Journey’s End",
    russian = "Провожающая в последний путь Фрирен",
    episodes = 28,
    id = "52991",
    kind = AnimeKindEnum.tv,
    poster = AnimeListQuery.Poster1(
        mainUrl = "https://example.com/anime_poster.jpg",
        mainAltUrl = "https://example.com/anime_poster_alt.jpg"
    ),
    rating = AnimeRatingEnum.pg_13,
    score = 9.2,
    season = "fall_2023",
    status = AnimeStatusEnum.released,
    genres = listOf(
        AnimeListQuery.Genre(id = "1", name = "Fantasy", russian = "Фэнтези"),
        AnimeListQuery.Genre(id = "2", name = "Adventure", russian = "Приключения")
    ),
    releasedOn = AnimeListQuery.ReleasedOn(day = 1, month = 9, year = 2023),
    studios = listOf(
        AnimeListQuery.Studio(id = "s1", name = "Madhouse", imageUrl = "https://example.com/anime_poster.jpg")
    )
)