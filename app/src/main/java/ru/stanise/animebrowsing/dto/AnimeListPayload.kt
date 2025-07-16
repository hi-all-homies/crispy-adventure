package ru.stanise.animebrowsing.dto

import kotlinx.serialization.Serializable

@Serializable
data class AnimeListPayload(val data: List<Anime>)
