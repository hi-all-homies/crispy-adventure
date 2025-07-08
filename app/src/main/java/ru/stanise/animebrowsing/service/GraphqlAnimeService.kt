package ru.stanise.animebrowsing.service

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import ru.stanise.animebrowsing.AnimeListQuery

class GraphqlAnimeService(private val apolloClient: ApolloClient) : AnimeService{

    override suspend fun getAnimeList(page: Int, limit: Int): List<AnimeListQuery.Anime> {
        val response = this.apolloClient.query(AnimeListQuery(
            page = Optional.present(page),
            limit = Optional.present(limit))
        )
            .execute()

        return response.dataOrThrow().animes
    }

}