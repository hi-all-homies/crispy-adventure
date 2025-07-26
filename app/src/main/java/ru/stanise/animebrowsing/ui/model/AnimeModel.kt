package ru.stanise.animebrowsing.ui.model

import android.util.Log
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.stanise.animebrowsing.config.AnimeApplication
import ru.stanise.animebrowsing.config.Config
import ru.stanise.animebrowsing.dto.Anime
import ru.stanise.animebrowsing.dto.AnimeType
import ru.stanise.animebrowsing.repository.AnimeRepo
import ru.stanise.animebrowsing.ui.nav.AppScreen
import ru.stanise.animebrowsing.ui.nav.Navigator

class AnimeModel(private val animeRepo: AnimeRepo, private val nav: Navigator) : ViewModel() {

    var animeList = mutableStateListOf<Anime>()
        private set

    var selectedAnime by mutableStateOf<Anime?>(null)
        private set

    private var currentPage = 1

    var isFetchingMore by mutableStateOf(false)
        private set

    var hasMorePages by mutableStateOf(true)
        private set


    private val _scrollTop = MutableSharedFlow<Unit>(replay = 1)
    val scrollTop: SharedFlow<Unit> = _scrollTop.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun resetScroll(){
        viewModelScope.launch {
            _scrollTop.resetReplayCache()
        }
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
                nav.navigateTo(AppScreen.AnimeList)
                _scrollTop.emit(Unit)
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


    fun selectAnime(anime: Anime){
        viewModelScope.launch {
            selectedAnime = anime
            nav.navigateTo(AppScreen.AnimeDetail)
        }
    }

    private fun handleRequest(
        requestBlock: suspend () -> List<Anime>,
        onSuccess: suspend (List<Anime>) -> Unit,
        onEmpty: suspend () -> Unit = { nav.navigateTo(AppScreen.NotFound) },
        onError: suspend () -> Unit = { nav.navigateTo(AppScreen.Error) }
    ){
        viewModelScope.launch {
            isFetchingMore = true
            try {
                val result = requestBlock()

                if (result.size < Config.LIMIT) {
                    hasMorePages = false
                }

                if (result.isEmpty()) {
                    onEmpty()
                }
                else {
                    onSuccess(result.distinctBy { it.id }.filter { it.type != AnimeType.MUSIC } )
                    currentPage++
                }
            }
            catch (e: Throwable){
                Log.d("ANIME_MODEL_HANDLE_REQUEST", "error message: ${e.message}")
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
                val nav = application.container.navigator
                AnimeModel(animeRepo, nav)
            }
        }
    }
}