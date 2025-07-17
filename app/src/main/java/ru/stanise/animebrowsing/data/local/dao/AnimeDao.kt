package ru.stanise.animebrowsing.data.local.dao

import androidx.room.*
import ru.stanise.animebrowsing.data.local.entity.AnimeEntity
import ru.stanise.animebrowsing.data.local.entity.AnimeGenreCrossRef
import ru.stanise.animebrowsing.data.local.entity.AnimeWithGenres
import ru.stanise.animebrowsing.dto.Genre

@Dao
interface AnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: AnimeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<Genre>)

    @Query("SELECT * FROM genre")
    suspend fun getGenres(): List<Genre>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeGenreCrossRefs(crossRefs: List<AnimeGenreCrossRef>)

    @Transaction
    @Query("SELECT * FROM anime WHERE id = :animeId")
    suspend fun getAnimeWithGenres(animeId: Int): AnimeWithGenres?

    @Transaction
    suspend fun insertAnimeWithGenres(anime: AnimeEntity, genres: List<Genre>) {
        insertAnime(anime)
        val crossRefs = genres.map { AnimeGenreCrossRef(animeId = anime.id, genreId = it.id) }
        insertAnimeGenreCrossRefs(crossRefs)
    }

}
