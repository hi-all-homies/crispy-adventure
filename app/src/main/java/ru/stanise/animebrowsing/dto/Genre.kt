package ru.stanise.animebrowsing.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    @SerialName("mal_id")
    val id: Int,
    val name: String
)