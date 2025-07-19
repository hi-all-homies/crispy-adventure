package ru.stanise.animebrowsing.ui.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.sqlite.SQLiteException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stanise.animebrowsing.config.AnimeApplication
import ru.stanise.animebrowsing.config.Config
import ru.stanise.animebrowsing.dto.Anime
import ru.stanise.animebrowsing.repository.FavesRepo

class FavesModel(private val favesRepo: FavesRepo) : ViewModel() {

    private val _faveUiState = MutableStateFlow(FavesUiState())
    val faveUiState = _faveUiState.asStateFlow()

    val favesState: StateFlow<List<Anime>> =
        favesRepo.getFaves()
            .retry(3) { cause ->
                Log.d("FAVES_STATE", "Retrying due to: ${cause.message}")
                cause is SQLiteException
            }
            .catch {ex ->
                Log.d("FAVES_STATE", "Error: ${ex.message}")
                _faveUiState.value = FavesUiState(hasError = true)
                emit(emptyList())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(Config.TIMEOUT_MILLIS),
                initialValue = listOf()
            )

    fun toggleFaves(anime: Anime) {
        viewModelScope.launch {
            try {
                favesRepo.toggleFaves(anime)
                _faveUiState.value = FavesUiState()
            }
            catch (ex: Throwable){
                Log.d("TOGGLE_FAVES", "Error: ${ex.message}")
                _faveUiState.value = FavesUiState(hasError = true)
            }
        }
    }

    fun updateFaveOnDelete(anime: Anime? = null){
        _faveUiState.update {
            it.copy(faveToDelete = anime, isShown = !it.isShown)
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as AnimeApplication)
                val favesRepo = application.container.favesRepo
                FavesModel(favesRepo)
            }
        }
    }
}

data class FavesUiState(
    val hasError: Boolean = false,
    val faveToDelete: Anime? = null,
    val isShown: Boolean = false
)