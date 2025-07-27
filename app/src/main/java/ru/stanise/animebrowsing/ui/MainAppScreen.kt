package ru.stanise.animebrowsing.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.stanise.animebrowsing.ui.model.AnimeModel
import ru.stanise.animebrowsing.ui.model.GenreModel
import ru.stanise.animebrowsing.ui.model.SearchModel
import ru.stanise.animebrowsing.ui.model.WindowSizeModel
import ru.stanise.animebrowsing.ui.nav.AppScreen
import ru.stanise.animebrowsing.ui.nav.NavCommand
import ru.stanise.animebrowsing.ui.nav.Navigator
import ru.stanise.animebrowsing.ui.nav.ObserveNavigation
import ru.stanise.animebrowsing.ui.nav.currentScreen
import ru.stanise.animebrowsing.ui.nav.enterTransition
import ru.stanise.animebrowsing.ui.nav.exitTransition
import ru.stanise.animebrowsing.ui.nav.popEnterTransition
import ru.stanise.animebrowsing.ui.nav.popExitTransition
import ru.stanise.animebrowsing.ui.nav.safeNavigate


@Composable
fun MainScreen(
    navigator: Navigator,
    windowSizeModel: WindowSizeModel,
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

    LaunchedEffect(Unit) {
        navigator.commands.collectLatest { command ->
            when (command) {
                is NavCommand.To -> {
                    navController.safeNavigate(command.screen.name)
                }
                is NavCommand.Back -> {
                    navController.popBackStack()
                }
            }
        }
    }

    ObserveNavigation(navController) {
        searchModel.closeSearchBar()
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
                searchModel = searchModel,
                currentScreen = screenState,
                onBackClick = {
                    if (screenState == AppScreen.NotFound)
                        animeModel.getAnimeList(searchModel.resetFilters())
                    else
                        navigator.back()
                },
                onSearch = { query ->
                    animeModel.getAnimeList(searchModel.searchByQuery(query))
                },
                toggleFilters = { visibleDialog = !visibleDialog },
                onToFaves = { navigator.navigateTo(AppScreen.Favorites) }
            )
        },
        snackbarHost = { SnackbarHost(snackHostState) }
    ) { innerPadding ->

        Box (modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = AppScreen.Launcher.name,
                enterTransition = { enterTransition },
                exitTransition = { exitTransition },
                popEnterTransition = { popEnterTransition },
                popExitTransition = { popExitTransition },
            ) {
                composable(AppScreen.Launcher.name) {
                    LauncherScreen {
                        genreModel.fetchGenres()
                        animeModel.getAnimeList(searchState)
                    }
                }

                composable(AppScreen.AnimeList.name) {
                    AnimeListScreen(animeModel, searchModel, windowSizeModel){
                        animeModel.selectAnime(it)
                    }
                }

                composable(AppScreen.AnimeDetail.name) {
                    selectedAnime?.let { anime ->
                        AnimeDetailScreen(
                            anime = anime,
                            onFaveAdded = onAdded,
                            onFaveRemoved = onRemoved,
                            windowSizeModel = windowSizeModel
                        )
                    }
                }

                composable(AppScreen.Favorites.name) {
                    FavoritesScreen(onGoTo = animeModel::selectAnime, onFaveRemoved = onRemoved, windowSizeModel = windowSizeModel)
                }

                composable(AppScreen.NotFound.name) {
                    NotFoundScreen { visibleDialog = !visibleDialog }
                }

                composable(AppScreen.Error.name) {
                    ErrorScreen({ animeModel.getAnimeList(searchState) })
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
            if (animeModel.isFetchingMore && animeModel.animeList.isEmpty()){
                LoadingOverlay()
            }
        }
    }
}