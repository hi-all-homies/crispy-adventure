package ru.stanise.animebrowsing.repository

import ru.stanise.animebrowsing.config.Config
import ru.stanise.animebrowsing.dto.Anime
import ru.stanise.animebrowsing.service.AnimeService
import ru.stanise.animebrowsing.ui.model.SearchUiState


class RetrofitAnimeRepo(val animeService: AnimeService) : AnimeRepo {

    override suspend fun getAnimeList(searchUiState: SearchUiState): List<Anime> {
        return animeService
            .getAnimeList(searchUiState.toQueryMap())
            .data
    }

    override suspend fun getAnimeById(id: Int): Anime {
        return animeService.getAnimeById(id).data
    }

    fun SearchUiState.toQueryMap(): Map<String, Any> = buildMap {
        if (query.isNotBlank()) {
            put("q", query)
        }

        if (minScore != 0f) {
            put("min_score", minScore)
        }

        if (selectedGenres.isNotEmpty()) {
            put("genres", selectedGenres.joinToString(","))
        }

        selectedKind.singleOrNull()?.let { put("type", it) }
        selectedStatus.singleOrNull()?.let { put("status", it) }

        put("page", page)
        put("limit", Config.LIMIT)
    }

}