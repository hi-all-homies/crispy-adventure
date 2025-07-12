package ru.stanise.animebrowsing.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.stanise.animebrowsing.ui.nav.AppScreen
import ru.stanise.animebrowsing.ui.theme.AnimeBrowsingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeTopBar(
    currentScreen: AppScreen,
    onBackClick: () -> Unit,
    onSearch: () -> Unit
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
            if (currentScreen != AppScreen.AnimeList && currentScreen != AppScreen.Loading) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search buton",
                    modifier = Modifier.size(40.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}


@Preview(showBackground = true)
@Composable
fun AppBarPreview(){
    AnimeBrowsingTheme {
        AnimeTopBar(AppScreen.NotFound,{}) { }
    }
}
