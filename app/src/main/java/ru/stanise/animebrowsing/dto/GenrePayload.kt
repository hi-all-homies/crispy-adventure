package ru.stanise.animebrowsing.dto

import kotlinx.serialization.Serializable

@Serializable
data class GenrePayload(val data: List<Genre>)
