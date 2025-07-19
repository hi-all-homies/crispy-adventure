package ru.stanise.animebrowsing.repository

import kotlinx.coroutines.flow.Flow
import ru.stanise.animebrowsing.dto.Anime
import ru.stanise.animebrowsing.dto.Genre

interface FavesRepo {

    suspend fun insertGenres(genres: List<Genre>)

    suspend fun getGenres(): List<Genre>

    fun getFaves(): Flow<List<Anime>>

    suspend fun toggleFaves(anime: Anime)
}