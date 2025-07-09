package ru.stanise.animebrowsing.ui.model

import ru.stanise.animebrowsing.AnimeListQuery

sealed class AnimeUiState {
    object Loading : AnimeUiState()
    data class Success(val data: List<AnimeListQuery.Anime>, var first: AnimeListQuery.Anime) : AnimeUiState()
    data class Error(val message: String) : AnimeUiState()

    data class NotFound(val message: String) : AnimeUiState()
}
