package ru.stanise.animebrowsing.config

import com.apollographql.apollo.ApolloClient
import ru.stanise.animebrowsing.repository.AnimeRepo
import ru.stanise.animebrowsing.repository.GraphqlAnimeRepo
import ru.stanise.animebrowsing.service.AnimeService
import ru.stanise.animebrowsing.service.GraphqlAnimeService

interface AppContainer {
    val animeRepo: AnimeRepo
}


class DefaultAppContainer : AppContainer {


    private val apollo = ApolloClient.Builder()
        .serverUrl(Config.BASE_URL)
        .build()

    private val animeService: AnimeService by lazy {
        GraphqlAnimeService(apollo)
    }

    override val animeRepo: AnimeRepo by lazy {
        GraphqlAnimeRepo(animeService)
    }
}