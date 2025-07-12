package ru.stanise.animebrowsing.repository

import ru.stanise.animebrowsing.AnimeListQuery
import ru.stanise.animebrowsing.ui.model.SearchUiState

interface AnimeRepo {

    suspend fun getAnimeList(searchUiState: SearchUiState) : List<AnimeListQuery.Anime>
}