package ru.stanise.animebrowsing.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.stanise.animebrowsing.dto.Anime
import ru.stanise.animebrowsing.ui.theme.AnimeBrowsingTheme
import ru.stanise.animebrowsing.R
import ru.stanise.animebrowsing.dto.AnimeType
import ru.stanise.animebrowsing.dto.Genre
import ru.stanise.animebrowsing.dto.Image
import ru.stanise.animebrowsing.dto.Images
import ru.stanise.animebrowsing.dto.Season
import ru.stanise.animebrowsing.dto.Status
import ru.stanise.animebrowsing.dto.Title

@Composable
fun AnimeDetailScreen(anime: Anime, modifier: Modifier = Modifier) {
    var synopsisExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            AnimePoster(
                anime.images.jpg.imageUrl,
                modifier = Modifier
                    .aspectRatio(2f / 3f)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Titles
            Text(
                text = "title",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            if (anime.titles.any { it.type == "Japanese" }) {
                Text(
                    text = "japanisse",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Genres
            if (anime.genres.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    anime.genres.forEach { genre ->
                        AssistChip(
                            onClick = {},
                            label = { Text(genre.name) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Info Row
            InfoRow(anime)

            Spacer(modifier = Modifier.height(12.dp))

            // Expandable synopsis
            anime.synopsis?.let { synopsis ->
                Column {
                    Text(
                        text = synopsis,
                        maxLines = if (synopsisExpanded) Int.MAX_VALUE else 4,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    TextButton(
                        onClick = { synopsisExpanded = !synopsisExpanded }
                    ) {
                        Text(if (synopsisExpanded) "Show less" else "Read more")
                    }
                }
            }
        }
//        if (anime.characterRoles?.isNotEmpty() == true) {
//            item {
//                Spacer(modifier = Modifier.height(16.dp))
//                CharacterList(anime.characterRoles)
//            }
//        }
    }
}


@Composable
private fun InfoRow(anime: Anime) {
    val infoItems = listOfNotNull(
        anime.season?.rawValue?.replaceFirstChar { it.uppercaseChar() },
        anime.year?.let { "Year: $it" },
        anime.type?.rawValue,
        anime.episodes?.takeIf { it > 0 }?.let { "$it eps" },
        anime.score?.let { "Score: $it" },
    )

    if (infoItems.isNotEmpty()) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            infoItems.forEach { label ->
                AssistChip(
                    onClick = {},
                    label = { Text(label) }
                )
            }
        }
    }
}


//@Composable
//fun CharacterList(
//    characterRoles: List<CharacterRole>,
//    modifier: Modifier = Modifier
//) {
//    Column(modifier = modifier) {
//        Text(
//            text = "Characters",
//            style = MaterialTheme.typography.titleMedium,
//            fontWeight = FontWeight.SemiBold,
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//
//        LazyRow(
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            items(characterRoles) { role ->
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier.width(100.dp)
//                ) {
//                    AsyncImage(
//                        model = role.character.poster?.mainUrl,
//                        contentDescription = role.character.name,
//                        contentScale = ContentScale.Crop,
//                        placeholder = painterResource(R.drawable.placeholder),
//                        error = painterResource(R.drawable.error),
//                        modifier = Modifier
//                            .size(100.dp)
//                            .clip(CircleShape)
//                    )
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = role.character.name,
//                        style = MaterialTheme.typography.bodySmall,
//                        textAlign = TextAlign.Center,
//                        maxLines = 2,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                }
//            }
//        }
//    }
//}


@Preview(showBackground = true)
@Composable
fun AnimeDetailPreview() {

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
        AnimeDetailScreen(stubAnime)
    }
}