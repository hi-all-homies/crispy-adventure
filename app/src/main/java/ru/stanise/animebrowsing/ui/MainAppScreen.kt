package ru.stanise.animebrowsing.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.stanise.animebrowsing.ui.model.AnimeModel
import ru.stanise.animebrowsing.ui.model.GenreModel
import ru.stanise.animebrowsing.ui.model.SearchModel
import ru.stanise.animebrowsing.ui.model.SearchUiState
import ru.stanise.animebrowsing.ui.nav.AppScreen
import ru.stanise.animebrowsing.ui.nav.NavCommand
import ru.stanise.animebrowsing.ui.nav.Navigator
import ru.stanise.animebrowsing.ui.nav.currentScreen
import ru.stanise.animebrowsing.ui.nav.safeNavigate


@Composable
fun MainScreen(
    navigator: Navigator,
    searchModel: SearchModel = viewModel(),
    animeModel: AnimeModel = viewModel(factory = AnimeModel.Factory),
    genreModel: GenreModel = viewModel(factory = GenreModel.Factory)
) {
    val navController = rememberNavController()
    val screenState = currentScreen(navController)

    val selectedAnime = animeModel.selectedAnime

    val searchState by searchModel.filters.collectAsState()

    val genreState by genreModel.genreState.collectAsState()

    var visibleDialog by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        navigator.commands.collectLatest { command ->
            focusManager.clearFocus(force = true)

            when (command) {
                is NavCommand.To -> {
                    navController.safeNavigate(
                        route = command.screen.name,
                        popUpToRoute = AppScreen.Launcher.name,
                        inclusive = true
                    )
                }
                is NavCommand.Back -> {
                    navController.popBackStack()
                }
            }
        }
    }

    val scope = rememberCoroutineScope()
    val snackHostState = remember { SnackbarHostState() }

    val onAdded: (String) -> Unit = {
        scope.launch {
            snackHostState.showSnackbar(message = "$it has been added to favorites")
        }
    }
    val onRemoved: (String) -> Unit = {
        scope.launch {
            snackHostState.showSnackbar(message = "$it has been removed from favorites")
        }
    }

    Scaffold(
        topBar = {
            AnimeTopBar(
                currentScreen = screenState,
                onBackClick = {
                    if (screenState == AppScreen.NotFound)
                        animeModel.getAnimeList(searchModel.resetFilters())
                    else
                        navigator.back()
                },
                toggleFilters = { visibleDialog = !visibleDialog },
                onSearch = { animeModel.getAnimeList(searchModel.searchByQuery(it)) },
                onToFaves = { navigator.navigateTo(AppScreen.Favorites) }
            )
        },
        snackbarHost = { SnackbarHost(snackHostState) }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = AppScreen.Launcher.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreen.Launcher.name) {
                LauncherScreen {
                    genreModel.fetchGenres()
                    animeModel.getAnimeList(SearchUiState())
                }
            }

            composable(AppScreen.AnimeList.name) {
                AnimeListScreen(animeModel, searchState){
                    animeModel.selectAnime(it)
                }
            }

            composable(AppScreen.AnimeDetail.name) {
                selectedAnime?.let { anime ->
                    AnimeDetailScreen(
                        anime = anime,
                        onFaveAdded = onAdded,
                        onFaveRemoved = onRemoved
                    )
                }
            }

            composable(AppScreen.Favorites.name) {
                FavoriteScreen(onGoTo = animeModel::selectAnime, onFaveRemoved = onRemoved)
            }

            composable(AppScreen.Loading.name) {
                LoadingScreen()
            }

            composable(AppScreen.NotFound.name) {
                NotFoundScreen { visibleDialog = !visibleDialog }
            }

            composable(AppScreen.Error.name) {
                ErrorScreen({})
            }
        }
        if (visibleDialog){
            FilterDialog(
                onDismiss = { visibleDialog = !visibleDialog },
                genres = genreState,
                onApply = {
                    animeModel.getAnimeList(searchModel.searchByFilters(it))
                    visibleDialog = false
                }
            )
        }
    }
}