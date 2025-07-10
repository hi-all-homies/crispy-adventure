package ru.stanise.animebrowsing.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stanise.animebrowsing.config.AnimeApplication
import ru.stanise.animebrowsing.repository.AnimeRepo
import ru.stanise.animebrowsing.AnimeListQuery
import ru.stanise.animebrowsing.ui.nav.AppScreen

class AnimeModel(private val animeRepo: AnimeRepo) : ViewModel() {

    private val _screenState = MutableStateFlow(AppScreen.Loading)
    val screenState = _screenState.asStateFlow()

    private val _animeState = MutableStateFlow(AnimeUiState(emptyList(), null))
    val animeState = _animeState.asStateFlow()


    init {
        getAnimeList(1, 5)
    }


    fun setScreenState(screen: AppScreen) {
        _screenState.value = screen
    }


    fun getAnimeList(page: Int, limit: Int) {
        handleRequest(
            { animeRepo.getAnimeList(page, limit) },
            {
                _animeState.value = AnimeUiState(it, it.first())
                _screenState.value = AppScreen.AnimeList
            }
        )
    }

    fun selectAnime(anime: AnimeListQuery.Anime){
        _animeState.update { it.copy(selectedAnime = anime) }
        _screenState.value = AppScreen.AnimeDetail
    }


    private fun handleRequest(
        requestBlock: suspend () -> List<AnimeListQuery.Anime>,
        onSuccess: (List<AnimeListQuery.Anime>) -> Unit,
        onEmpty: () -> Unit = { _screenState.value = AppScreen.NotFound },
        onError: () -> Unit = { _screenState.value = AppScreen.Error }
    ){
        viewModelScope.launch {
            _screenState.value = AppScreen.Loading
            try {
                val result = requestBlock()

                if (result.isEmpty())
                    onEmpty()
                else
                    onSuccess(result)
            }
            catch (_: Throwable){
                onError()
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