package ru.stanise.animebrowsing.config

import android.content.Context
import androidx.room.Room
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.stanise.animebrowsing.data.local.db.AppDatabase
import ru.stanise.animebrowsing.dto.OffsetDateTimeSerializer
import ru.stanise.animebrowsing.repository.AnimeRepo
import ru.stanise.animebrowsing.repository.CharacterRepo
import ru.stanise.animebrowsing.repository.FavesRepo
import ru.stanise.animebrowsing.repository.GenreRepo
import ru.stanise.animebrowsing.repository.RetrofitAnimeRepo
import ru.stanise.animebrowsing.repository.RetrofitCharacterRepo
import ru.stanise.animebrowsing.repository.RetrofitGenreRepo
import ru.stanise.animebrowsing.repository.RoomFavesRepo
import ru.stanise.animebrowsing.service.AnimeService
import ru.stanise.animebrowsing.service.CharacterService
import ru.stanise.animebrowsing.service.GenreService
import ru.stanise.animebrowsing.ui.nav.Navigator
import java.time.OffsetDateTime


interface AppContainer {
    val animeRepo: AnimeRepo
    val navigator: Navigator
    val characterRepo: CharacterRepo
    val genreRepo: GenreRepo
    val favesRepo: FavesRepo
}


class DefaultAppContainer(context: Context) : AppContainer {
    private val contentType = "application/json".toMediaType()

    val timeSerializersModule = SerializersModule {
        contextual(OffsetDateTime::class, OffsetDateTimeSerializer)
    }
    private val json = Json {
        ignoreUnknownKeys = true
        serializersModule = timeSerializersModule
    }



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

    private val genreService: GenreService by lazy {
        retrofit.create(GenreService::class.java)
    }

    override val genreRepo: GenreRepo by lazy {
        RetrofitGenreRepo(genreService)
    }

    private val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "anime_database")
            .fallbackToDestructiveMigration(true)
            .build()
    }

    override val favesRepo: FavesRepo by lazy {
        RoomFavesRepo(appDatabase.animeDao())
    }
}