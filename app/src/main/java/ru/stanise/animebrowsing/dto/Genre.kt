package ru.stanise.animebrowsing.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity("genre")
data class Genre(
    @SerialName("mal_id")
    @PrimaryKey
    val id: Int,
    val name: String
)