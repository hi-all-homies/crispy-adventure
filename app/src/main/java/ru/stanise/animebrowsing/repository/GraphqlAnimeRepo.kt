package ru.stanise.animebrowsing.repository

import ru.stanise.animebrowsing.AnimeListQuery
import ru.stanise.animebrowsing.service.AnimeService

class GraphqlAnimeRepo(private val animeService: AnimeService) : AnimeRepo {

    override suspend fun getAnimeList(page: Int, limit: Int): List<AnimeListQuery.Anime> {
        return this.animeService.getAnimeList(page, limit)
    }
}