package ru.stanise.animebrowsing.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ru.stanise.animebrowsing.ui.model.SearchModel
import ru.stanise.animebrowsing.ui.nav.AppScreen
import ru.stanise.animebrowsing.ui.theme.AnimeBrowsingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeTopBar(
    searchModel: SearchModel,
    currentScreen: AppScreen?,
    onBackClick: () -> Unit,
    onSearch: (String) -> Unit,
    toggleFilters: () -> Unit,
    onToFaves: () -> Unit
) {
    val isSearchMode by searchModel.isSearchMode.collectAsState()

    BackHandler(enabled = isSearchMode) {
        searchModel.closeSearchBar()
    }

    val focusRequester = remember { FocusRequester() }

    var query by rememberSaveable { mutableStateOf("") }

    TopAppBar(
        title = {
            AnimatedContent(
                targetState = isSearchMode,
                transitionSpec = {
                    slideInHorizontally().togetherWith(slideOutHorizontally())
                },
                label = "SearchModeTransition"
            ) { inSearchMode ->
                if (inSearchMode) {
                    LaunchedEffect(Unit) {
                        delay(100)
                        focusRequester.requestFocus()
                    }

                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        label = { Text("Search by title") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .padding(horizontal = 8.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            onSearch(query)
                            query = ""
                            searchModel.closeSearchBar()
                        })
                    )
                } else {
                    Text("Anime browsing")
                }
            }
        },
        navigationIcon = {
            if (currentScreen != AppScreen.AnimeList) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { searchModel.toggleSearchBar() } ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Toggle Search",
                    modifier = Modifier.size(28.dp)
                )
            }
            TopBarMenu(
                onOpen = { searchModel.closeSearchBar() },
                toggleFilters = toggleFilters,
                toFaves = onToFaves
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}


@Composable
fun TopBarMenu(
    onOpen: () -> Unit,
    toggleFilters: () -> Unit,
    toFaves: () -> Unit
){
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = {
            onOpen()
            expanded = !expanded
        }
        ) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options", modifier = Modifier.size(28.dp))
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("search filters") },
                onClick = {
                    toggleFilters()
                    expanded = false
                },
                trailingIcon = { Icon(Icons.Default.FilterList, contentDescription = "toggle filters") }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("faves anime") },
                onClick = {
                    toFaves()
                    expanded = false
                },
                trailingIcon = { Icon(Icons.Default.Favorite, contentDescription = "favorites screen") }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppBarPreview(){
    val searchModel = remember { SearchModel() }
    AnimeBrowsingTheme {
        AnimeTopBar(searchModel,AppScreen.NotFound, {}, {}, {}, {})
    }
}
