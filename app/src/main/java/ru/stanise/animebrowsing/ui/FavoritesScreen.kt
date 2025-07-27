package ru.stanise.animebrowsing.ui

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.stanise.animebrowsing.dto.Anime
import ru.stanise.animebrowsing.ui.model.FavesModel
import ru.stanise.animebrowsing.ui.model.FavesUiState
import ru.stanise.animebrowsing.ui.model.WindowSizeModel
import kotlin.math.abs


@Composable
fun FavoritesScreen(
    onGoTo: (Anime) -> Unit,
    onFaveRemoved: (String) -> Unit,
    windowSizeModel: WindowSizeModel,
    modifier: Modifier = Modifier,
    favesModel: FavesModel = viewModel(factory = FavesModel.Factory)
) {
    val faves by favesModel.favesState.collectAsState()
    val faveOnDelete by favesModel.faveUiState.collectAsState()
    val windowSizeState by windowSizeModel.windowWidthState.collectAsState()

    when(windowSizeState){
        WindowWidthSizeClass.Compact -> {
            CompactFavesScreen(
                faves = faves,
                faveOnDelete = faveOnDelete,
                toggleFaves = favesModel::toggleFaves,
                onDelete = favesModel::updateFaveOnDelete,
                onGoTo = onGoTo,
                onFaveRemoved = onFaveRemoved,
                windowWidthSizeClass = windowSizeState,
                modifier = modifier
            )
        }
        else -> {
            ExpandedFavesScreen(
                faves = faves,
                faveOnDelete = faveOnDelete,
                toggleFaves = favesModel::toggleFaves,
                onDelete = favesModel::updateFaveOnDelete,
                onGoTo = onGoTo,
                onFaveRemoved = onFaveRemoved,
                windowWidthSizeClass = windowSizeState,
                modifier = modifier
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactFavesScreen(
    faves: List<Anime>,
    faveOnDelete: FavesUiState,
    toggleFaves: (Anime) -> Unit,
    onDelete: (Anime?) -> Unit,
    onGoTo: (Anime) -> Unit,
    onFaveRemoved: (String) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    modifier: Modifier = Modifier
){
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val swipeThreshold = with(LocalDensity.current) { 20.dp.toPx() }

    Column(modifier = modifier.fillMaxSize()) {
        PrimaryTabRow(selectedTabIndex = selectedTab) {
            tabItems.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(tab.title, style = MaterialTheme.typography.titleMedium) },
                    icon = {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = "${tab.title} tab"
                        )
                    },
                    unselectedContentColor = MaterialTheme.colorScheme.secondary
                )
            }
        }
        Box(
            modifier.pointerInput(Unit){
                detectHorizontalDragGestures { change, dragAmount ->
                    if (abs(dragAmount) >= swipeThreshold){
                        selectedTab = if (dragAmount > 0) 0 else 1
                    }
                }
            }
        ) {
            when(selectedTab){
                0 -> FavesTab(
                    faves = faves,
                    faveOnDelete = faveOnDelete,
                    toggleFaves = toggleFaves,
                    onDelete = onDelete,
                    onGoTo = onGoTo,
                    onFaveRemoved = onFaveRemoved
                )
                1 -> GenresTab(faves, windowWidthSizeClass)
            }
        }
    }
}


@Composable
fun ExpandedFavesScreen(
    faves: List<Anime>,
    faveOnDelete: FavesUiState,
    toggleFaves: (Anime) -> Unit,
    onDelete: (Anime?) -> Unit,
    onGoTo: (Anime) -> Unit,
    onFaveRemoved: (String) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    modifier: Modifier = Modifier
){
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Row(modifier = modifier.fillMaxSize()) {

        NavigationRail {
            tabItems.forEachIndexed { index, tab ->
                NavigationRailItem(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    icon = {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = "${tab.title} tab"
                        )
                    },
                    label = {
                        Text(tab.title, style = MaterialTheme.typography.titleMedium)
                    },
                    alwaysShowLabel = true
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            when (selectedTab) {
                0 -> FavesTab(
                    faves = faves,
                    faveOnDelete = faveOnDelete,
                    toggleFaves = toggleFaves,
                    onDelete = onDelete,
                    onGoTo = onGoTo,
                    onFaveRemoved = onFaveRemoved
                )
                1 -> GenresTab(faves, windowWidthSizeClass)
            }
        }
    }

}


val tabItems = listOf(
    TabItem("faves anime", Icons.Outlined.FavoriteBorder),
    TabItem("genre statistics", Icons.Outlined.PieChart)
)

data class TabItem(val title: String, val icon: ImageVector)