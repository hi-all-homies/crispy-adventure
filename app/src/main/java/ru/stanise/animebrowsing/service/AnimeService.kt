package ru.stanise.animebrowsing.service

import ru.stanise.animebrowsing.AnimeListQuery
import ru.stanise.animebrowsing.ui.model.SearchUiState

interface AnimeService {

    suspend fun getAnimeList(searchQuery: SearchUiState) : List<AnimeListQuery.Anime>
}