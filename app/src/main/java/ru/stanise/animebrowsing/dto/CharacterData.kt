package ru.stanise.animebrowsing.dto

import kotlinx.serialization.Serializable

@Serializable
data class CharacterData(
    val role: String,
    val character: Character
)
