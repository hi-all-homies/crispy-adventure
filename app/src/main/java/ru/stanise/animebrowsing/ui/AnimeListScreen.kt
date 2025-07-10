package ru.stanise.animebrowsing.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.stanise.animebrowsing.AnimeListQuery
import ru.stanise.animebrowsing.ui.theme.AnimeBrowsingTheme

@Composable
fun AnimeListScreen(
    animeList: List<AnimeListQuery.Anime>,
    modifier: Modifier = Modifier,
    onAnimeClick: (AnimeListQuery.Anime) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(animeList, key = { it.id }) { anime ->
            AnimeListItem(
                anime = anime,
                onClick = { onAnimeClick(anime) }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAnimeListScreen() {
    AnimeBrowsingTheme {
        AnimeListScreen(
            animeList = List(5) { sampleAnime.copy(id = it.toString()) },
            onAnimeClick = {}
        )
    }
}
