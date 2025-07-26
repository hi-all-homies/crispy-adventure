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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.stanise.animebrowsing.dto.Aired
import ru.stanise.animebrowsing.dto.Anime
import ru.stanise.animebrowsing.dto.AnimeType
import ru.stanise.animebrowsing.dto.Genre
import ru.stanise.animebrowsing.dto.Image
import ru.stanise.animebrowsing.dto.Images
import ru.stanise.animebrowsing.dto.Season
import ru.stanise.animebrowsing.dto.Status
import ru.stanise.animebrowsing.dto.Title
import ru.stanise.animebrowsing.dto.getEnglishTitleOrFallback
import ru.stanise.animebrowsing.dto.getSeasonYear
import ru.stanise.animebrowsing.ui.theme.AnimeBrowsingTheme
import java.time.OffsetDateTime


@Composable
fun AnimeListItem(
    anime: Anime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AnimePoster(
                anime.images.webp.imageUrl,
                modifier = Modifier
                    .height(185.dp)
                    .aspectRatio(2f / 3f)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = anime.titles.getEnglishTitleOrFallback(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                anime.status?.let { StatusChip(it) }

                anime.let {
                    val label = it.getSeasonYear()
                    if (label.isNotBlank()) {
                        LabeledIconRow(label, Icons.Default.DateRange, MaterialTheme.typography.bodyMedium)
                    }
                }

                anime.type?.let {
                    Text(
                        text = it.rawValue,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                anime.score?.let {
                    LabeledIconRow(it.toString(), Icons.Default.Star, MaterialTheme.typography.bodyMedium)
                }

                GenresRow(anime)
            }
        }
    }
}

@Composable
fun LabeledIconRow(text: String, icon: ImageVector, textStyle: TextStyle){
    Row(
        modifier = Modifier.padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            style = textStyle,
            text = text
        )
    }
}


@Composable
fun GenresRow(anime: Anime, modifier: Modifier = Modifier){
    val genreChipsScroll = rememberScrollState()
    val genreList = anime.genres + anime.themes + anime.demographics

    Row(
        modifier = modifier
            .padding(top = 4.dp)
            .horizontalScroll(genreChipsScroll)
    ) {
        genreList.forEach { genre ->
            AssistChip(
                onClick = {},
                label = { Text(genre.name) },
                modifier = modifier.padding(end = 4.dp)
            )
        }
    }
}


@Composable
fun StatusChip(status: Status){
    val color = if (status == Status.COMPLETE) MaterialTheme.colorScheme.error else if (status == Status.AIRING) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
    AssistChip(
        onClick = {},
        label = { Text(text = status.rawValue) },
        colors = AssistChipDefaults.assistChipColors(labelColor = color),
        border = AssistChipDefaults.assistChipBorder(enabled = true, borderColor = color),
    )
}


@Preview
@Composable
fun PreviewAnimeListItem() {

    val stubAnime = Anime(
        id = 1,
        images = Images( // You should create a stub for Images too
            webp = Image(
                imageUrl = "https://example.com/image.jpg",
                largeImageUrl = "https://example.com/image.jpg"
            )
        ),
        titles = listOf(
            Title(type = "Default", title = "Stub Anime Title"),
            Title(type = "English", title = "Stub Anime Title EN")
        ),
        type = AnimeType.MOVIE,
        episodes = 12,
        status = Status.COMPLETE,
        duration = "24 min per ep",
        score = 8.5,
        synopsis = "This is a stub synopsis for the anime.",
        background = "Some background info.",
        season = Season.SPRING,
        year = 2025,
        genres = listOf(
            Genre(id = 1, name = "Action"),
            Genre(id = 2, name = "Adventure")
        ),
        themes = listOf(
            Genre(id = 66, name = "Mahou Shoujo")
        ),
        demographics = listOf(
            Genre(id = 42, name = "Seinen")
        ),
        aired = Aired(
            from = OffsetDateTime.parse("2005-04-15T00:00:00+00:00"),
            to = OffsetDateTime.parse("2005-09-27T00:00:00+00:00")
        )
    )

    AnimeBrowsingTheme {
        AnimeListItem(anime = stubAnime, {})
    }
}
