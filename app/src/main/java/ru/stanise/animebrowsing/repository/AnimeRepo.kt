package ru.stanise.animebrowsing.repository

import ru.stanise.animebrowsing.AnimeListQuery

interface AnimeRepo {

    suspend fun getAnimeList(page: Int, limit: Int) : List<AnimeListQuery.Anime>
}