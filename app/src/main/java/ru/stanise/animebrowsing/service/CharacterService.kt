package ru.stanise.animebrowsing.service

import retrofit2.http.GET
import retrofit2.http.Path
import ru.stanise.animebrowsing.dto.CharacterPayload
import ru.stanise.animebrowsing.dto.SingleCharacterPayload

interface CharacterService {

    @GET("anime/{id}/characters")
    suspend fun getAnimeCharacters(@Path("id") id: Int) : CharacterPayload

    @GET("characters/{id}")
    suspend fun getCharacter(@Path("id") id: Int) : SingleCharacterPayload
}