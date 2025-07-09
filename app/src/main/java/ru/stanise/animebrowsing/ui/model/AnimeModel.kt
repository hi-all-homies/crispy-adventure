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
import ru.stanise.animebrowsing.repository.AnimeRepo
import ru.stanise.animebrowsing.AnimeListQuery

class AnimeModel(private val animeRepo: AnimeRepo) : ViewModel() {
    private val _ui = MutableStateFlow<AnimeUiState>(AnimeUiState.Loading)

    val ui = _ui.asStateFlow()


    init {
        getAnimeList(1, 5)
    }


    fun getAnimeList(page: Int, limit: Int) {
        if (_ui.value !is AnimeUiState.Loading) {
            _ui.value = AnimeUiState.Loading
        }
        Log.d("ANIME", "running getAnimeList()")
        viewModelScope.launch {
            try {
                val result = animeRepo.getAnimeList(page, limit)
                if (result.isEmpty()) {
                    _ui.value = AnimeUiState.NotFound("No results.")
                } else {
                    _ui.value = AnimeUiState.Success(result, result.first())
                }
            } catch (e: Exception) {
                _ui.value = AnimeUiState.Error(e.localizedMessage ?: "Unexpected error")
            }
        }
    }

    fun changeAnimeDetail(anime: AnimeListQuery.Anime){
        (_ui.value as AnimeUiState.Success).first = anime
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