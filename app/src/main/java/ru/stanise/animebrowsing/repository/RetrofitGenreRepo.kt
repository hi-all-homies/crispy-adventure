package ru.stanise.animebrowsing.repository

import ru.stanise.animebrowsing.dto.Genre
import ru.stanise.animebrowsing.service.GenreService

class RetrofitGenreRepo(private val genreService: GenreService) : GenreRepo {

    override suspend fun getGenres(): List<Genre> {
        return genreService.getAvailableGenres().data
    }
}