package ru.stanise.animebrowsing.data.local.dao

import androidx.room.*
import ru.stanise.animebrowsing.data.local.entity.AnimeEntity
import ru.stanise.animebrowsing.data.local.entity.AnimeGenreCrossRef
import ru.stanise.animebrowsing.data.local.entity.AnimeWithGenres
import ru.stanise.animebrowsing.dto.Genre
import kotlinx.coroutines.flow.Flow

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
    @Query("SELECT * FROM anime")
    fun getAnimeWithGenres(): Flow<List<AnimeWithGenres>>

    @Query("SELECT * FROM anime WHERE anime.id = :id")
    suspend fun getAnimeById(id: Int): AnimeEntity?

    @Delete
    suspend fun deleteAnime(anime: AnimeEntity)

    @Transaction
    suspend fun toggleFaves(anime: AnimeEntity, genres: List<Genre>) {
        val alreadyStored = getAnimeById(anime.id)
        if (alreadyStored != null){
            deleteAnime(alreadyStored)
        }
        else {
            insertAnime(anime)
            val crossRefs = genres.map { AnimeGenreCrossRef(animeId = anime.id, genreId = it.id) }
            insertAnimeGenreCrossRefs(crossRefs)
        }
    }
}
