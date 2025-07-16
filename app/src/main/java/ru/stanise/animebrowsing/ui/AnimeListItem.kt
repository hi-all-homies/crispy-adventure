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
import ru.stanise.animebrowsing.dto.Anime
import ru.stanise.animebrowsing.dto.AnimeType
import ru.stanise.animebrowsing.dto.Genre
import ru.stanise.animebrowsing.dto.Image
import ru.stanise.animebrowsing.dto.Images
import ru.stanise.animebrowsing.dto.Season
import ru.stanise.animebrowsing.dto.Status
import ru.stanise.animebrowsing.dto.Title
import ru.stanise.animebrowsing.dto.getEnglishTitleOrFallback
import ru.stanise.animebrowsing.ui.theme.AnimeBrowsingTheme


@Composable
fun AnimeListItem(
    anime: Anime,
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
                anime.images.jpg.imageUrl,
                modifier = Modifier
                    .height(180.dp)
                    .aspectRatio(2f / 3f)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = anime.titles.getEnglishTitleOrFallback(),
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

                if (anime.genres.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        anime.genres.forEach { genre ->
                            AssistChip(
                                onClick = {},
                                label = { Text(genre.name) },
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

    val stubAnime = Anime(
        id = 1,
        images = Images( // You should create a stub for Images too
            jpg = Image(
                imageUrl = "https://example.com/image.jpg",
                largeImageUrl = "https://example.com/image.jpg"
            )
        ),
        titles = listOf(
            Title(type = "Default", title = "Stub Anime Title"),
            Title(type = "English", title = "Stub Anime Title EN")
        ),
        type = AnimeType.TV,
        episodes = 12,
        status = Status.COMPLETE,
        duration = "24 min per ep",
        score = 8.5,
        synopsis = "This is a stub synopsis for the anime.",
        background = "Some background info.",
        season = Season.SPRING,
        year = 2022,
        genres = listOf(
            Genre(id = 1, name = "Action"),
            Genre(id = 2, name = "Adventure")
        ),
        themes = listOf(
            Genre(id = 66, name = "Mahou Shoujo")
        ),
        demographics = listOf(
            Genre(id = 42, name = "Seinen")
        )
    )

    AnimeBrowsingTheme {
        AnimeListItem(anime = stubAnime, {})
    }
}
