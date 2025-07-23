package ru.stanise.animebrowsing.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import ru.stanise.animebrowsing.ui.model.FavesUiState
import java.time.OffsetDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavesTab(
    faves: List<Anime>,
    faveOnDelete: FavesUiState,
    modifier: Modifier = Modifier,
    toggleFaves: (Anime) -> Unit,
    onDelete: (Anime?) -> Unit,
    onGoTo: (Anime) -> Unit,
    onFaveRemoved: (String) -> Unit
){
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(faves, { it.id }){
            FaveAnimeItem(it, { onGoTo(it) }, onDelete = { onDelete(it) })
        }
    }

    if (faveOnDelete.isShown){
        DeleteDialog(
            onConfirm = {
                faveOnDelete.faveToDelete?.let {
                    toggleFaves(it)
                    onFaveRemoved(it.titles.getEnglishTitleOrFallback())
                }
            },
            onDismiss = { onDelete(null) }
        )
    }
}

@Composable
fun FaveAnimeItem(
    anime: Anime,
    onGoTo: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            AnimePoster(
                anime.images.webp.imageUrl,
                modifier = modifier
                    .height(50.dp)
                    .aspectRatio(2f / 3f)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Text(
                text = anime.titles.getEnglishTitleOrFallback(),
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
                modifier = modifier.weight(1f)
            )

            IconButton(onClick = onGoTo) {
                Icon(
                    imageVector = Icons.Default.ArrowOutward,
                    contentDescription = "go to",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "delete a fave",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun DeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
){
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) {
                Text(text = "Ok")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        },
        title = {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Are you sure?", style = MaterialTheme.typography.titleMedium)
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewFaveAnimeItem(){
    val stubAnime = Anime(
        id = 1,
        images = Images( // You should create a stub for Images too
            webp = Image(
                imageUrl = "https://example.com/image.jpg",
                largeImageUrl = "https://example.com/image.jpg"
            )
        ),
        titles = listOf(
            Title(type = "English", title = "Stub Anime Title Stub Anime Title Stub Anime Title Stub Anime"),
            Title(type = "Default", title = "Stub Anime Title EN")
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
    FaveAnimeItem(stubAnime, {}, {})
}