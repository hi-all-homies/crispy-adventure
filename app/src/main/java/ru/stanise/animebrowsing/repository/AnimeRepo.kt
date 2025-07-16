package ru.stanise.animebrowsing.repository


import ru.stanise.animebrowsing.dto.Anime
import ru.stanise.animebrowsing.ui.model.SearchUiState

interface AnimeRepo {
    suspend fun getAnimeList(searchUiState: SearchUiState) : List<Anime>

    suspend fun getAnimeById(id: Int): Anime
}