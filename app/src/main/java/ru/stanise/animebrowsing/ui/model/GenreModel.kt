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
import ru.stanise.animebrowsing.dto.Genre
import ru.stanise.animebrowsing.repository.FavesRepo
import ru.stanise.animebrowsing.repository.GenreRepo

class GenreModel(private val genreRepo: GenreRepo, private val favesRepo: FavesRepo) : ViewModel() {

    private val _genreState = MutableStateFlow<List<Genre>>(emptyList())
    val genreState = _genreState.asStateFlow()


    fun fetchGenres(){
        viewModelScope.launch {
            try {
                _genreState.value = favesRepo.getGenres()
                    .ifEmpty {
                        val fetched = genreRepo.getGenres()
                        favesRepo.insertGenres(fetched)
                        fetched
                    }
                    .filter { it in availableGenres }

            }
            catch (ex: Throwable){
                _genreState.value = availableGenres.toList()
                Log.d("FETCH_GENRES", "message: ${ex.message}")
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as AnimeApplication)
                val genreRepo = application.container.genreRepo
                val favesRepo = application.container.favesRepo
                GenreModel(genreRepo, favesRepo)
            }
        }
    }
}