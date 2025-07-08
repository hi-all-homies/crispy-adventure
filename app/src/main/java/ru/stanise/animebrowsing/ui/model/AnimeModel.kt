package ru.stanise.animebrowsing.ui.model

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
import ru.stanise.animebrowsing.repository.AnimeRepo

class AnimeModel(private val animeRepo: AnimeRepo) : ViewModel() {
    private val _ui = MutableStateFlow<AnimeUiState>(AnimeUiState.Loading)

    val ui = _ui.asStateFlow()


    fun getAnimeList(page: Int, limit: Int) {
        viewModelScope.launch {
            try {
                val anime = animeRepo.getAnimeList(page, limit)
                _ui.value = AnimeUiState.Success(anime)
            }
            catch (exp: Throwable) {
                _ui.value = AnimeUiState.Error(exp.message ?: "unknown error")
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as AnimeApplication)
                val animeRepo = application.container.animeRepo
                AnimeModel(animeRepo)
            }
        }
    }
}