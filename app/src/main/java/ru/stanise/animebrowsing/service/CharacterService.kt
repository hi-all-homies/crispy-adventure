package ru.stanise.animebrowsing.service

import retrofit2.http.GET
import retrofit2.http.Path
import ru.stanise.animebrowsing.dto.CharacterPayload

interface CharacterService {

    @GET("anime/{id}/characters")
    suspend fun getAnimeCharacters(@Path("id") id: Int) : CharacterPayload
}