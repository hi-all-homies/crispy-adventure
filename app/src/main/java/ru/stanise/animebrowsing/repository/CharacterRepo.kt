package ru.stanise.animebrowsing.repository

import ru.stanise.animebrowsing.dto.CharacterData

interface CharacterRepo {

    suspend fun getAnimeCharacters(id: Int) : List<CharacterData>
}