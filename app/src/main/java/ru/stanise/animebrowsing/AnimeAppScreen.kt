package ru.stanise.animebrowsing

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.stanise.animebrowsing.ui.AnimeDetailScreen
import ru.stanise.animebrowsing.ui.AnimeListScreen
import ru.stanise.animebrowsing.ui.AnimeTopBar
import ru.stanise.animebrowsing.ui.ErrorScreen
import ru.stanise.animebrowsing.ui.FilterDialog
import ru.stanise.animebrowsing.ui.LoadingScreen
import ru.stanise.animebrowsing.ui.NotFoundScreen
import ru.stanise.animebrowsing.ui.model.AnimeModel
import ru.stanise.animebrowsing.ui.model.SearchModel
import ru.stanise.animebrowsing.ui.model.availableGenres
import ru.stanise.animebrowsing.ui.nav.AppScreen


@Composable
fun AnimeAppScreen() {
    val navController = rememberNavController()

    val animeModel: AnimeModel = viewModel(factory = AnimeModel.Factory)
    val screenState by animeModel.screenState.collectAsState()
    val selectedAnime = animeModel.selectedAnime

    val searchModel: SearchModel = viewModel()
    val searchState by searchModel.filters.collectAsState()

    var lastScreen by rememberSaveable { mutableStateOf<AppScreen?>(null) }

    var visibleDialog by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    LaunchedEffect(screenState) {
        if (screenState != lastScreen && screenState != AppScreen.Loading) {
            lastScreen = screenState
            focusManager.clearFocus(force = true)
            navController.navigate(screenState.name) {
                launchSingleTop = true
            }
        }
    }

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            AppScreen.entries.find { it.name == destination.route }?.let {
                animeModel.setScreenState(it)
            }
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
                        navController.popBackStack()
                },
                toggleFilters = { visibleDialog = !visibleDialog },
                onSearch = { animeModel.getAnimeList(searchModel.searchByQuery(it)) }
            )
        }
    ) { innerPadding ->
        if (screenState == AppScreen.Loading) {
            LoadingScreen(modifier = Modifier.padding(innerPadding))
        } else {
            NavHost(
                navController = navController,
                startDestination = AppScreen.AnimeList.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(AppScreen.AnimeList.name) {
                    AnimeListScreen(animeModel, searchState){
                        animeModel.selectAnime(it)
                    }
                }

                composable(AppScreen.AnimeDetail.name) {
                    selectedAnime?.let { anime ->
                        AnimeDetailScreen(anime)
                    }
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
                    genres = availableGenres,
                    onApply = {
                        animeModel.getAnimeList(searchModel.searchByFilters(it))
                        visibleDialog = false
                    }
                )
            }
        }
    }
}
