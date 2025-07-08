package ru.stanise.animebrowsing.service

import ru.stanise.animebrowsing.AnimeListQuery

interface AnimeService {

    suspend fun getAnimeList(page: Int, limit: Int) : List<AnimeListQuery.Anime>
}