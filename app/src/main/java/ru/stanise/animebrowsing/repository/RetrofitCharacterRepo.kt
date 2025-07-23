package ru.stanise.animebrowsing.repository

import ru.stanise.animebrowsing.dto.Character
import ru.stanise.animebrowsing.dto.CharacterData
import ru.stanise.animebrowsing.service.CharacterService

class RetrofitCharacterRepo(private val characterService: CharacterService) : CharacterRepo {

    override suspend fun getAnimeCharacters(id: Int): List<CharacterData> {
        return characterService.getAnimeCharacters(id).data
    }

    override suspend fun getCharacter(id: Int): Character {
        return this.characterService.getCharacter(id).data
    }
}