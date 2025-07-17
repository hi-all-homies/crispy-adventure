package ru.stanise.animebrowsing.repository

import ru.stanise.animebrowsing.dto.Genre

interface GenreRepo {

    suspend fun getGenres(): List<Genre>
}