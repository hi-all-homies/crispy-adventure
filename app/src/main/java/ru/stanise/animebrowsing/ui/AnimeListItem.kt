package ru.stanise.animebrowsing.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.stanise.animebrowsing.AnimeListQuery
import ru.stanise.animebrowsing.ui.theme.AnimeBrowsingTheme


@Composable
fun AnimeListItem(
    anime: AnimeListQuery.Anime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AnimePoster(
                anime.poster?.mainUrl ?: anime.poster?.mainAltUrl,
                modifier = Modifier
                    .height(180.dp)
                    .aspectRatio(2f / 3f)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = anime.russian ?: anime.english ?: "Untitled",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Episodes: ${anime.episodes}",
                    style = MaterialTheme.typography.bodySmall
                )

                anime.score?.let {
                    Text(
                        text = "⭐ $it",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                if (!anime.genres.isNullOrEmpty()) {
                    Row(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        anime.genres.forEach { genre ->
                            AssistChip(
                                onClick = {},
                                label = { Text(genre.russian) },
                                modifier = Modifier
                                    .padding(end = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewAnimeListItem() {
    AnimeBrowsingTheme {
        AnimeListItem(anime = sampleAnime, {})
    }
}
