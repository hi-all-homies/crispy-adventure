package ru.stanise.animebrowsing.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.stanise.animebrowsing.dto.Genre
import ru.stanise.animebrowsing.dto.allowedStatuses
import ru.stanise.animebrowsing.dto.allowedTypes
import ru.stanise.animebrowsing.ui.model.SearchUiState
import ru.stanise.animebrowsing.ui.model.SearchUiStateSaver
import ru.stanise.animebrowsing.ui.model.availableGenres
import kotlin.math.roundToInt


@Composable
fun FilterDialog(
    genres: List<Genre>,
    onDismiss: () -> Unit,
    onApply: (SearchUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchState by rememberSaveable(stateSaver = SearchUiStateSaver) {
        mutableStateOf(SearchUiState(topRated = false))
    }

    val onGenreToggle: (String) -> Unit = { genreId ->
        val current = searchState.selectedGenres
        val updated = if (genreId in current) current - genreId else current + genreId
        searchState = searchState.copy(selectedGenres = updated)
    }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(dismissOnClickOutside = false, usePlatformDefaultWidth = false, decorFitsSystemWindows = false)) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                CollapsibleFilterSection(
                    title = "Genres",
                    items = genres,
                    selectedItems = searchState.selectedGenres,
                    itemId = { it.id.toString() },
                    itemLabel = { it.name },
                    onItemToggle =  onGenreToggle,
                    onSelectCancel = onGenreToggle,
                    onClearAll = { searchState = searchState.copy(selectedGenres = emptySet()) }
                )

                Spacer(Modifier.height(12.dp))

                CollapsibleFilterSection(
                    title = "Status",
                    items = allowedStatuses,
                    selectedItems = searchState.selectedStatus,
                    itemId = { it.rawValue },
                    itemLabel = { it.rawValue},
                    onItemToggle = { searchState = searchState.copy(selectedStatus = setOf(it)) },
                    onSelectCancel = { searchState = searchState.copy(selectedStatus = emptySet()) },
                    onClearAll = { searchState = searchState.copy(selectedStatus = emptySet()) }
                )

                Spacer(Modifier.height(12.dp))

                CollapsibleFilterSection(
                    title = "Kind",
                    items = allowedTypes,
                    selectedItems = searchState.selectedKind,
                    itemId = { it.rawValue },
                    itemLabel = { it.rawValue},
                    onItemToggle = { searchState = searchState.copy(selectedKind = setOf(it)) },
                    onSelectCancel = { searchState = searchState.copy(selectedKind = emptySet()) },
                    onClearAll = { searchState = searchState.copy(selectedKind = emptySet()) }
                )

                Spacer(Modifier.height(12.dp))

                Text("Min score: ${searchState.minScore.roundToInt()}", style = MaterialTheme.typography.titleMedium)
                Slider(
                    value = searchState.minScore,
                    onValueChange = { searchState = searchState.copy(minScore = it) },
                    valueRange = 1f..9f,
                    steps = 7
                )

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onApply(searchState) })  {
                        Text("Apply")
                    }
                }
            }
        }
    }
}


@Composable
fun <T>CollapsibleFilterSection(
    title: String,
    items: List<T>,
    selectedItems: Set<String>,
    itemId: (T) -> String,
    itemLabel: (T) -> String,
    onItemToggle: (String) -> Unit,
    onSelectCancel: (String) -> Unit,
    onClearAll: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )

            TextButton(
                enabled = selectedItems.isNotEmpty(),
                onClick = onClearAll,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Text("Clear All")
            }

            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (expanded) "Collapse" else "Expand"
            )
        }

        AnimatedVisibility(visible = expanded) {
            FlowRow(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items.forEach { item ->
                    val id = itemId(item)
                    val label = itemLabel(item)
                    val isSelected = id in selectedItems

                    FilterChip(
                        selected = isSelected,
                        onClick = { onItemToggle(id) },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
        }

        if (selectedItems.isNotEmpty() && !expanded) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items.filter { itemId(it) in selectedItems }.forEach { item ->
                    AssistChip(
                        onClick = { onSelectCancel(itemId(item)) },
                        label = { Text(itemLabel(item)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove ${itemLabel(item)}",
                                modifier = Modifier.size(AssistChipDefaults.IconSize)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GenreSelectorPreview() {
    val selectedGenres = remember { mutableStateSetOf<String>()  }

    CollapsibleFilterSection(
        title = "Genres",
        items = availableGenres.toList(),
        selectedItems = selectedGenres,
        { it.id.toString() },
        { it.name },
        { it ->
            if (selectedGenres.contains(it)) selectedGenres.remove(it) else selectedGenres.add(it)
        },
        { if (selectedGenres.contains(it)) selectedGenres.remove(it) else selectedGenres.add(it) },
        {selectedGenres.clear()}
    )
}