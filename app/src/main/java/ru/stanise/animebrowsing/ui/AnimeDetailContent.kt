package ru.stanise.animebrowsing.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
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
import ru.stanise.animebrowsing.dto.getJapaneseTitleOrFallback
import ru.stanise.animebrowsing.dto.getSeasonYear
import ru.stanise.animebrowsing.ui.theme.AnimeBrowsingTheme

@Composable
fun AnimeDetailScreen(anime: Anime, modifier: Modifier = Modifier) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            AnimePoster(
                anime.images.jpg.imageUrl,
                modifier = modifier
                    .aspectRatio(2f / 3f)
                    .clip(MaterialTheme.shapes.medium)
            )
        }

        item {
            Spacer(modifier = modifier.height(16.dp))
            Text(
                text = anime.titles.getEnglishTitleOrFallback(),
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            anime.titles.getJapaneseTitleOrFallback().let {
                if (it.isNotBlank()){
                    Spacer(modifier = modifier.height(8.dp))

                    Text(
                        text = anime.titles.getJapaneseTitleOrFallback(),
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = modifier.height(8.dp))

            anime.type?.let {
                Text(
                    text = it.rawValue,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = modifier.height(8.dp))

            anime.let {
                val label = it.getSeasonYear()
                if (label.isNotBlank()) {
                    LabeledIconRow(label, Icons.Default.DateRange, MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(modifier = modifier.height(8.dp))

            anime.score?.let {
                LabeledIconRow(it.toString(), Icons.Default.Star, MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = modifier.height(8.dp))

            anime.episodes?.let {
                Text(
                    text = "Episodes: $it",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            Spacer(modifier = modifier.height(8.dp))

            anime.duration?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            Spacer(modifier = modifier.height(8.dp))
            GenresRow(anime)
            Spacer(modifier = modifier.height(8.dp))
        }

        item {
            anime.synopsis?.let { ExpandableText(it) }
            Spacer(modifier = modifier.height(12.dp))
            anime.background?.let { if (it.isNotBlank()) ExpandableText("Background: \n$it") }
        }

        item {
            Spacer(modifier = modifier.height(12.dp))
            CharacterList(anime.id)
        }
    }
}


@Composable
fun ExpandableText(
    text: String,
    collapsedMaxLines: Int = 2,
    showToggleIcon: Boolean = true,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    fadeColor: Color = MaterialTheme.colorScheme.background
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var isOverflowing by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .animateContentSize()
    ) {
        Box {
            Text(
                text = text,
                maxLines = if (expanded) Int.MAX_VALUE else collapsedMaxLines,
                style = textStyle,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { result ->
                    isOverflowing = result.hasVisualOverflow && !expanded
                },
                modifier = modifier
                    .fillMaxWidth()
            )

            if (!expanded && isOverflowing) {
                Box(
                    modifier = modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, fadeColor),
                                startY = 0f,
                                endY = 100f
                            )
                        )
                )
            }
        }

        if (showToggleIcon && isOverflowing) {
            IconButton(
                onClick = { expanded = !expanded },
                modifier = modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Show less" else "Show more"
                )
            }
        }
    }
}



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
        synopsis = "This is a stub synopsis for the anime.This is a stub synopsis for the anime.This is a stub synopsis for the anime.This is a stub synopsis for the anime.This is a stub synopsis for the anime.This is a stub synopsis for the anime.This is a stub synopsis for the anime.This is a stub synopsis for the anime.This is a stub synopsis for the anime.",
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