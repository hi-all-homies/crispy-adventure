package ru.stanise.animebrowsing.dto

import kotlinx.serialization.Serializable

@Serializable
data class CharacterPayload(
    val data: List<CharacterData>
)
