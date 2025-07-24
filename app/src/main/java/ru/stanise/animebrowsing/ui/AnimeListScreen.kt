package ru.stanise.animebrowsing.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.stanise.animebrowsing.dto.Anime
import ru.stanise.animebrowsing.ui.model.AnimeModel
import ru.stanise.animebrowsing.ui.model.SearchModel

@Composable
fun AnimeListScreen(
    animeModel: AnimeModel,
    searchModel: SearchModel,
    modifier: Modifier = Modifier,
    onAnimeClick: (Anime) -> Unit
) {
    val searchState by searchModel.filters.collectAsState()
    val animeList = animeModel.animeList
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val showScrollButton = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 2 ||
                    (listState.firstVisibleItemIndex == 2 && listState.firstVisibleItemScrollOffset > 150)
        }
    }


    Box(modifier.fillMaxSize()){
        LazyColumn(
            state = listState,
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            itemsIndexed(animeList, key = { ind, it -> "${it.id}_$ind" }) {_, anime ->
                AnimeListItem(
                    anime = anime,
                    onClick = { onAnimeClick(anime) }
                )
            }
        }

        if (showScrollButton.value && !listState.isScrollInProgress){
            ScrollButton(
                onClick = { scope.launch { listState.scrollToItem(0) } },
                modifier = modifier.align(Alignment.BottomEnd).padding(20.dp)
            )
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val visibleItems = layoutInfo.visibleItemsInfo
            val lastVisibleIndex = visibleItems.lastOrNull()?.index ?: 0
            lastVisibleIndex to totalItems
        }
            .distinctUntilChanged()
            .collectLatest { (lastVisibleIndex, totalItems) ->
                if (lastVisibleIndex >= totalItems - 5) {
                    animeModel.getNextPage(searchState)
                }
            }
    }
}

@Composable
fun ScrollButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    SmallFloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary
    ) {
        Icon(Icons.Filled.ArrowUpward, "to beginning")
    }
}