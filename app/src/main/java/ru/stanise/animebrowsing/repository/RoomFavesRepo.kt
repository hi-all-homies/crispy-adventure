package ru.stanise.animebrowsing.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import ru.stanise.animebrowsing.data.local.dao.AnimeDao
import ru.stanise.animebrowsing.dto.Anime
import ru.stanise.animebrowsing.dto.Genre
import ru.stanise.animebrowsing.dto.toDto
import ru.stanise.animebrowsing.dto.toEntityWithGenres


class RoomFavesRepo(private val animeDao: AnimeDao) : FavesRepo {

    override suspend fun insertGenres(genres: List<Genre>) {
        return animeDao.insertGenres(genres)
    }

    override suspend fun getGenres(): List<Genre> {
        return animeDao.getGenres()
    }

    override fun getFaves(): Flow<List<Anime>> {
        return animeDao.getAnimeWithGenres()
            .filterNotNull()
            .map {fave ->
                fave.map { it.toDto() }
            }
    }

    override suspend fun toggleFaves(anime: Anime) {
        val pair = anime.toEntityWithGenres()
        animeDao.toggleFaves(pair.first, pair.second)
    }
}