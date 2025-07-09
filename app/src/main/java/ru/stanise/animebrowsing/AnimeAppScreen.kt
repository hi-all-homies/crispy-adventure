package ru.stanise.animebrowsing

import android.util.Log
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
import ru.stanise.animebrowsing.ui.ErrorScreen
import ru.stanise.animebrowsing.ui.LoadingScreen
import ru.stanise.animebrowsing.ui.NotFoundScreen
import ru.stanise.animebrowsing.ui.model.AnimeModel
import ru.stanise.animebrowsing.ui.model.AnimeUiState
import ru.stanise.animebrowsing.ui.nav.AppScreen


@Composable
fun AnimeAppScreen() {
    val navController = rememberNavController()

    val animeModel: AnimeModel = viewModel(factory = AnimeModel.Factory)
    val animeUiState by animeModel.ui.collectAsState()

    var lastScreen by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(animeUiState) {
        val targetScreen = when (animeUiState) {
            is AnimeUiState.Success -> AppScreen.AnimeList.name
            is AnimeUiState.NotFound -> AppScreen.NotFound.name
            else -> AppScreen.Error.name
        }

        if (animeUiState !is AnimeUiState.Loading && targetScreen != lastScreen) {
            lastScreen = targetScreen
            Log.d("ANIME", "running navigate(${targetScreen})")
            navController.navigate(targetScreen) {
                launchSingleTop = true
            }
        }
    }

    Scaffold(
        topBar = {},
        bottomBar = {}
    ) { innerPadding ->
        if (animeUiState is AnimeUiState.Loading) {
            LoadingScreen(modifier = Modifier.padding(innerPadding))
        } else {
            NavHost(
                navController = navController,
                startDestination = AppScreen.AnimeList.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(AppScreen.AnimeList.name) {
                    (animeUiState as? AnimeUiState.Success)?.let {
                        AnimeListScreen(it.data, {anime ->
                            animeModel.changeAnimeDetail(anime)
                            navController.navigate(AppScreen.AnimeDetail.name){
                                launchSingleTop = true
                            }
                        })
                    }
                }

                composable(AppScreen.AnimeDetail.name) {
                    (animeUiState as? AnimeUiState.Success)?.let {
                        AnimeDetailScreen(it.first)
                    }
                }

                composable(AppScreen.NotFound.name) {
                    NotFoundScreen()
                }

                composable(AppScreen.Error.name) {
                    ErrorScreen({})
                }
            }
        }
    }
}
