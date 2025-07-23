package ru.stanise.animebrowsing.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.BookmarkRemove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.stanise.animebrowsing.dto.Aired
import ru.stanise.animebrowsing.dto.Anime
import ru.stanise.animebrowsing.dto.AnimeType
import ru.stanise.animebrowsing.dto.Character
import ru.stanise.animebrowsing.dto.CharacterData
import ru.stanise.animebrowsing.dto.Genre
import ru.stanise.animebrowsing.dto.Image
import ru.stanise.animebrowsing.dto.Images
import ru.stanise.animebrowsing.dto.Season
import ru.stanise.animebrowsing.dto.Status
import ru.stanise.animebrowsing.dto.Title
import ru.stanise.animebrowsing.dto.getEnglishTitleOrFallback
import ru.stanise.animebrowsing.dto.getJapaneseTitleOrFallback
import ru.stanise.animebrowsing.dto.getSeasonYear
import ru.stanise.animebrowsing.repository.CharacterRepo
import ru.stanise.animebrowsing.repository.FavesRepo
import ru.stanise.animebrowsing.ui.model.CharacterModel
import ru.stanise.animebrowsing.ui.model.FavesModel
import ru.stanise.animebrowsing.ui.theme.AnimeBrowsingTheme
import java.time.OffsetDateTime

@Composable
fun AnimeDetailScreen(
    anime: Anime,
    onFaveAdded: (String) -> Unit,
    onFaveRemoved: (String) -> Unit,
    modifier: Modifier = Modifier,
    favesModel: FavesModel = viewModel(factory = FavesModel.Factory),
    characterModel: CharacterModel = viewModel(factory = CharacterModel.Factory)
) {
    val faves by favesModel.favesState.collectAsState()
    val faveOnDelete by favesModel.faveUiState.collectAsState()
    val characters by characterModel.characters.collectAsState()

    LaunchedEffect(Unit) {
        characterModel.getAnimeCharacters(anime.id)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item{
            Column(
                modifier = modifier.fillMaxWidth()
            ) {
                Text(
                    text = anime.titles.getEnglishTitleOrFallback(),
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 4,
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
            }
        }
        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
            ) {
                AnimePoster(
                    anime.images.webp.imageUrl,
                    modifier = modifier
                        .height(385.dp)
                        .aspectRatio(2f / 3f)
                        .clip(MaterialTheme.shapes.medium)
                )
            }
        }

        item {
            Column(
                modifier = modifier
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier.fillMaxWidth()
                ) {
                    val isBookmarked = faves.any { it.id == anime.id }

                    anime.status?.let { StatusChip(it) }

                    IconButton(onClick = {
                        if (isBookmarked) favesModel.updateFaveOnDelete(anime)
                        else {
                            favesModel.toggleFaves(anime)
                            onFaveAdded(anime.titles.getEnglishTitleOrFallback())
                        }
                    }) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Outlined.BookmarkRemove else Icons.Outlined.BookmarkAdd,
                            contentDescription = if (isBookmarked) "delete fave" else "add to faves",
                            tint = if (isBookmarked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    }
                }

                AiredRow(anime, modifier)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(28.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier.fillMaxWidth()
                ) {
                    anime.score?.let {
                        LabeledIconRow(it.toString(), Icons.Default.Star, MaterialTheme.typography.bodyLarge)
                    }

                    anime.type?.let {
                        Text(
                            text = it.rawValue.uppercase(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = modifier.height(8.dp))

                anime.let {
                    val label = it.getSeasonYear()
                    if (label.isNotBlank()) {
                        LabeledIconRow(label, Icons.Default.DateRange, MaterialTheme.typography.bodyLarge)
                    }
                }

                Spacer(modifier = modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(28.dp)
                ) {
                    anime.episodes?.let {
                        Text(
                            text = "Episodes: $it",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }

                    anime.duration?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }

        item {
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
            CharacterList(characters, characterModel)
        }
    }

    if (faveOnDelete.isShown){
        DeleteDialog(
            onConfirm = {
                faveOnDelete.faveToDelete?.let {
                    favesModel.toggleFaves(it)
                    onFaveRemoved(anime.titles.getEnglishTitleOrFallback())
                }
            },
            onDismiss = favesModel::updateFaveOnDelete
        )
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

@Composable
fun AiredRow(anime: Anime, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        anime.aired.from?.let {
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = "since ${it.dayOfMonth} ${it.month.name.lowercase()} ${it.year}"
            )
        }
        anime.aired.to?.let {
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = "to ${it.dayOfMonth} ${it.month.name.lowercase()} ${it.year}"
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun AnimeDetailPreview() {

    val stubAnime = Anime(
        id = 1,
        images = Images( // You should create a stub for Images too
            webp = Image(
                imageUrl = "https://example.com/image.jpg",
                largeImageUrl = "https://example.com/image.jpg"
            )
        ),
        titles = listOf(
            Title(type = "Default", title = "Stub Anime Title "),
            Title(type = "English", title = "Stub Anime Title EN Stub Anime Title Stub Anime Title Stub Anime Title Stub Anime Title Stub Anime Title")
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
        ),
        aired = Aired(
            from = OffsetDateTime.parse("2005-04-15T00:00:00+00:00"),
            to = OffsetDateTime.parse("2005-09-27T00:00:00+00:00")
        )
    )

    val favesModel = remember {
        val favesRepo = object : FavesRepo {
            override suspend fun insertGenres(genres: List<Genre>) {}
            override suspend fun getGenres(): List<Genre> { return emptyList() }
            override fun getFaves(): Flow<List<Anime>> { return flowOf(emptyList()) }
            override suspend fun toggleFaves(anime: Anime) {}
        }
        FavesModel(favesRepo)
    }

    val characterModel = remember {
        val charRepo = object : CharacterRepo {
            override suspend fun getAnimeCharacters(id: Int): List<CharacterData> { return emptyList() }
            override suspend fun getCharacter(id: Int): Character { return Character(id = 1, name = "Name", images = Images(webp = Image(imageUrl = "https...")))
            }
        }
        CharacterModel(charRepo)
    }

    AnimeBrowsingTheme {
        AnimeDetailScreen(stubAnime, {}, {}, favesModel = favesModel, characterModel = characterModel)
    }
}