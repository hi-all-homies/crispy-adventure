package ru.stanise.animebrowsing.service

import retrofit2.http.GET
import ru.stanise.animebrowsing.dto.GenrePayload

interface GenreService {

    @GET("genres/anime")
    suspend fun getAvailableGenres(): GenrePayload
}