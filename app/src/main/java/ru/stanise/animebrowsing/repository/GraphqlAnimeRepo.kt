package ru.stanise.animebrowsing.repository

import ru.stanise.animebrowsing.AnimeListQuery
import ru.stanise.animebrowsing.service.AnimeService
import ru.stanise.animebrowsing.ui.model.SearchUiState

class GraphqlAnimeRepo(private val animeService: AnimeService) : AnimeRepo {

    override suspend fun getAnimeList(searchUiState: SearchUiState): List<AnimeListQuery.Anime> {
        return this.animeService.getAnimeList(searchUiState)
    }
}