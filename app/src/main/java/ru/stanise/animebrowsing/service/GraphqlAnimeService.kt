package ru.stanise.animebrowsing.service

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import ru.stanise.animebrowsing.AnimeListQuery
import ru.stanise.animebrowsing.ui.model.SearchUiState
import kotlin.math.roundToInt

class GraphqlAnimeService(private val apolloClient: ApolloClient) : AnimeService{

    override suspend fun getAnimeList(searchQuery: SearchUiState): List<AnimeListQuery.Anime> {
        return createQuery(searchQuery)
            .execute()
            .dataOrThrow()
            .animes
    }

    private fun createQuery(searchQuery: SearchUiState): ApolloCall<AnimeListQuery.Data> {
        val search = searchQuery.query.ifBlank { null }
        val score = searchQuery.minScore.let {
            val rndVal = it.roundToInt()
            if (rndVal != 0) rndVal else null
        }

        val genreString = searchQuery.selectedGenres.reduceOrNull { acc, string ->
            acc.plus(",").plus(string)
        }

        val kind = searchQuery.selectedKind.singleOrNull()
        val status = searchQuery.selectedStatus.singleOrNull()

        val query = apolloClient.query(AnimeListQuery(
            page = Optional.present(searchQuery.page),
            limit = Optional.present(25),
            censored = Optional.present(false),
            search = Optional.presentIfNotNull(search),
            kind = Optional.presentIfNotNull(kind),
            status = Optional.presentIfNotNull(status),
            score = Optional.presentIfNotNull(score),
            genre = Optional.presentIfNotNull(genreString))
        )
        return query
    }
}