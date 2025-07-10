package ru.stanise.animebrowsing.ui.model

import ru.stanise.animebrowsing.AnimeListQuery

data class AnimeUiState(
    val animeList: List<AnimeListQuery.Anime>,
    val selectedAnime: AnimeListQuery.Anime?
)
