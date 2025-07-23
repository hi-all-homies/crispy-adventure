package ru.stanise.animebrowsing.ui.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.stanise.animebrowsing.config.AnimeApplication
import ru.stanise.animebrowsing.dto.Character
import ru.stanise.animebrowsing.dto.CharacterData
import ru.stanise.animebrowsing.repository.CharacterRepo

class CharacterModel(private val characterRepo: CharacterRepo) : ViewModel() {

    private val _characters = MutableStateFlow<List<CharacterData>>(emptyList())
    val characters = _characters.asStateFlow()

    private val _singleCharState = MutableStateFlow<SingleCharacterUiState>(SingleCharacterUiState.Loading)
    val singleCharState = _singleCharState.asStateFlow()

    fun getAnimeCharacters(id: Int){
        viewModelScope.launch {
            try {
                _characters.value = characterRepo.getAnimeCharacters(id)
            }
            catch (ex: Throwable){
                _characters.value = emptyList()
                Log.d("GET_ANIME_CHARACTERS", "message: ${ex.message}")
            }
        }
    }


    fun getCharacterById(id: Int){
        viewModelScope.launch {
            _singleCharState.value = SingleCharacterUiState.Loading
            try {
                _singleCharState.value = SingleCharacterUiState.Success(characterRepo.getCharacter(id))
            }
            catch (ex: Throwable){
                _singleCharState.value = SingleCharacterUiState.Error("${ex.message}")
                Log.d("GET_CHARACTER_BY_ID", "message: ${ex.message}")
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as AnimeApplication)
                val characterRepo = application.container.characterRepo
                CharacterModel(characterRepo)
            }
        }
    }
}


sealed class SingleCharacterUiState(){
    object Loading : SingleCharacterUiState()
    data class Success(val single: Character) : SingleCharacterUiState()
    data class  Error(val message: String) : SingleCharacterUiState()
}