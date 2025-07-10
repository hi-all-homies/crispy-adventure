package ru.stanise.animebrowsing.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.stanise.animebrowsing.ui.nav.AppScreen
import ru.stanise.animebrowsing.ui.theme.AnimeBrowsingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeTopBar(
    currentScreen: AppScreen,
    canNavigateBack: Boolean,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = when (currentScreen) {
                    AppScreen.AnimeList -> "Anime List"
                    AppScreen.AnimeDetail -> "Anime Details"
                    AppScreen.NotFound -> "Not Found"
                    AppScreen.Error -> "Error"
                    AppScreen.Loading -> "Loading"
                },
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}


@Preview(showBackground = true)
@Composable
fun AppBarPreview(){
    AnimeBrowsingTheme {
        AnimeTopBar(AppScreen.NotFound, true) { }
    }
}
