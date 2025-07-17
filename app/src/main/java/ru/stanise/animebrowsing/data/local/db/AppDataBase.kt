package ru.stanise.animebrowsing.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.stanise.animebrowsing.data.local.converter.Converters
import ru.stanise.animebrowsing.data.local.dao.AnimeDao
import ru.stanise.animebrowsing.data.local.entity.AnimeEntity
import ru.stanise.animebrowsing.data.local.entity.AnimeGenreCrossRef
import ru.stanise.animebrowsing.dto.Genre

@Database(
    entities = [AnimeEntity::class, Genre::class, AnimeGenreCrossRef::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao
}
