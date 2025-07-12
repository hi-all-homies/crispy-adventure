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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.stanise.animebrowsing.ui.AnimeDetailScreen
import ru.stanise.animebrowsing.ui.AnimeListScreen
import ru.stanise.animebrowsing.ui.AnimeSearchBottomSheet
import ru.stanise.animebrowsing.ui.AnimeTopBar
import ru.stanise.animebrowsing.ui.ErrorScreen
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
    val animeState by animeModel.animeState.collectAsState()

    val searchModel: SearchModel = viewModel()
    val searchState by searchModel.filters.collectAsState()

    var lastScreen by rememberSaveable { mutableStateOf<AppScreen?>(null) }

    var visibleBottomSearch by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(screenState) {
        if (screenState != lastScreen && screenState != AppScreen.Loading) {
            lastScreen = screenState
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
                canNavigateBack = screenState != AppScreen.AnimeList,
                onBackClick = { navController.popBackStack() },
                onSearch = { visibleBottomSearch = true }
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
                    AnimeListScreen(animeState.animeList){
                        animeModel.selectAnime(it)
                    }
                }

                composable(AppScreen.AnimeDetail.name) {
                    animeState.selectedAnime?.let { anime ->
                        AnimeDetailScreen(anime)
                    }
                }

                composable(AppScreen.NotFound.name) {
                    NotFoundScreen()
                }

                composable(AppScreen.Error.name) {
                    ErrorScreen({})
                }
            }
            AnimeSearchBottomSheet(
                visible = visibleBottomSearch,
                onDismiss = { visibleBottomSearch = !visibleBottomSearch },
                searchText = searchState.query,
                onSearchTextChange = searchModel::updateQuery,
                selectedKind = searchState.selectedKind,
                onKindSelected = searchModel::updateKind,
                selectedStatus = searchState.selectedStatus,
                onStatusSelected = searchModel::updateStatus,
                score = searchState.minScore,
                onScoreChange = searchModel::updateScore,
                genres = availableGenres,
                selectedGenres = searchState.selectedGenres,
                onGenreToggle = searchModel::toggleGenre,
                onApply = {
                    animeModel.getAnimeList(searchModel.applyFilters())
                    visibleBottomSearch = false
                },
                onReset = searchModel::resetFilters
            )
        }
    }
}
