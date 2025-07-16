package ru.stanise.animebrowsing.config

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.stanise.animebrowsing.repository.AnimeRepo
import ru.stanise.animebrowsing.repository.CharacterRepo
import ru.stanise.animebrowsing.repository.RetrofitAnimeRepo
import ru.stanise.animebrowsing.repository.RetrofitCharacterRepo
import ru.stanise.animebrowsing.service.AnimeService
import ru.stanise.animebrowsing.service.CharacterService
import ru.stanise.animebrowsing.ui.nav.Navigator


interface AppContainer {
    val animeRepo: AnimeRepo
    val navigator: Navigator
    val characterRepo: CharacterRepo
}


class DefaultAppContainer : AppContainer {
    private val contentType = "application/json".toMediaType()
    private val json = Json { ignoreUnknownKeys = true }



    private val retrofit = Retrofit.Builder()
        .baseUrl(Config.BASE_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    private val animeService: AnimeService by lazy {
        retrofit.create(AnimeService::class.java)
    }

    override val animeRepo: AnimeRepo by lazy {
        RetrofitAnimeRepo(animeService)
    }

    override val navigator: Navigator by lazy {
        Navigator()
    }

    private val characterService: CharacterService by lazy {
        retrofit.create(CharacterService::class.java)
    }
    override val characterRepo: CharacterRepo by lazy {
        RetrofitCharacterRepo(characterService)
    }
}