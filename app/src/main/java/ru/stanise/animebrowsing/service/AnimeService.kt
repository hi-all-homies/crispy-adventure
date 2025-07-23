package ru.stanise.animebrowsing.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.stanise.animebrowsing.config.Config
import ru.stanise.animebrowsing.dto.AnimePayload
import ru.stanise.animebrowsing.dto.AnimeListPayload

interface AnimeService {

    @GET("anime")
    suspend fun getAnimeList(@QueryMap params: Map<String, @JvmSuppressWildcards Any>) : AnimeListPayload

    @GET("anime/{id}")
    suspend fun getAnimeById(@Path("id") id: Int) : AnimePayload

    @GET("top/anime")
    suspend fun getTopRatedAnimeList(@Query("page") page: Int, @Query("limit") limit: Int = Config.LIMIT) : AnimeListPayload
}