package ru.stanise.animebrowsing.ui.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import ru.stanise.animebrowsing.ui.nav.AppScreen

class AnimeModel(private val animeRepo: AnimeRepo) : ViewModel() {

    private val _screenState = MutableStateFlow(AppScreen.Loading)
    val screenState = _screenState.asStateFlow()

    var animeList = mutableStateListOf<AnimeListQuery.Anime>()
        private set

    var selectedAnime by mutableStateOf<AnimeListQuery.Anime?>(null)
        private set

    private var currentPage = 1
    private val limit = 25

    var isFetchingMore by mutableStateOf(false)
        private set

    var hasMorePages by mutableStateOf(true)
        private set

    init {
        getAnimeList(SearchUiState())
    }


    fun setScreenState(screen: AppScreen) {
        _screenState.value = screen
    }


    fun getAnimeList(searchUiState: SearchUiState) {
        currentPage = 1
        hasMorePages = true
        animeList.clear()

        handleRequest(
            { animeRepo.getAnimeList(searchUiState) },
            {
                animeList.addAll(it)
                selectedAnime = it.first()
                _screenState.value = AppScreen.AnimeList
            }
        )
    }


    fun getNextPage(searchUiState: SearchUiState){
        if (isFetchingMore || !hasMorePages) return

        handleRequest(
            {
                animeRepo.getAnimeList(searchUiState.copy(page = currentPage))
            },
            { animeList.addAll(it) },
            { hasMorePages = false },
            { hasMorePages = false }
        )
    }


    fun selectAnime(anime: AnimeListQuery.Anime){
        selectedAnime = anime
        _screenState.value = AppScreen.AnimeDetail
    }


    private fun handleRequest(
        requestBlock: suspend () -> List<AnimeListQuery.Anime>,
        onSuccess: (List<AnimeListQuery.Anime>) -> Unit,
        onEmpty: () -> Unit = { _screenState.value = AppScreen.NotFound },
        onError: () -> Unit = { _screenState.value = AppScreen.Error }
    ){
        isFetchingMore = true
        viewModelScope.launch {
            if (currentPage == 1){
                _screenState.value = AppScreen.Loading
            }
            try {
                val result = requestBlock()

                if (result.size < limit) {
                    hasMorePages = false
                }

                if (result.isEmpty()) {
                    onEmpty()
                }
                else {
                    onSuccess(result)
                    currentPage++
                }
            }
            catch (_: Throwable){
                onError()
            }
            finally {
                isFetchingMore  = false
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