package ru.stanise.animebrowsing.data.local.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import ru.stanise.animebrowsing.dto.AnimeType
import ru.stanise.animebrowsing.dto.Season
import ru.stanise.animebrowsing.dto.Status

@ProvidedTypeConverter
object Converters {

    @TypeConverter
    fun fromAnimeType(value: AnimeType?): String? = value?.name

    @TypeConverter
    fun toAnimeType(value: String?): AnimeType? = value?.let { AnimeType.valueOf(it) }

    @TypeConverter
    fun fromStatus(value: Status?): String? = value?.name

    @TypeConverter
    fun toStatus(value: String?): Status? = value?.let { Status.valueOf(it) }

    @TypeConverter
    fun fromSeason(value: Season?): String? = value?.name

    @TypeConverter
    fun toSeason(value: String?): Season? = value?.let { Season.valueOf(it) }
}
