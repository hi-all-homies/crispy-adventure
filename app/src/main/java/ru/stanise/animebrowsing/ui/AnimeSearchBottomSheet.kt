package ru.stanise.animebrowsing.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.stanise.animebrowsing.AnimeListQuery
import ru.stanise.animebrowsing.type.AnimeKindEnum
import ru.stanise.animebrowsing.type.AnimeStatusEnum
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeSearchBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    selectedKind: AnimeKindEnum?,
    onKindSelected: (AnimeKindEnum?) -> Unit,
    selectedStatus: AnimeStatusEnum?,
    onStatusSelected: (AnimeStatusEnum?) -> Unit,
    score: Float,
    onScoreChange: (Float) -> Unit,
    selectedGenres: Set<String>,
    onGenreToggle: (String) -> Unit,
    genres: List<AnimeListQuery.Genre>,
    onApply: () -> Unit,
    onReset: () -> Unit
){
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (visible){
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text("Search", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    placeholder = { Text("Search by title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Text("Kind", style = MaterialTheme.typography.titleMedium)
                DropdownSelector(
                    label = "label kind",
                    items = AnimeKindEnum.knownEntries,
                    selected = selectedKind,
                    onItemSelected = onKindSelected,
                    itemLabel = {it.name}
                )

                Spacer(Modifier.height(16.dp))

                Text("Status", style = MaterialTheme.typography.titleMedium)
                DropdownSelector(
                    label = "label status",
                    items = AnimeStatusEnum.knownEntries,
                    selected = selectedStatus,
                    onItemSelected = onStatusSelected,
                    itemLabel = {it.name}
                )

                Spacer(Modifier.height(16.dp))

                Text("Score: ${score.roundToInt()}", style = MaterialTheme.typography.titleMedium)
                Slider(
                    value = score,
                    onValueChange = onScoreChange,
                    valueRange = 1f..10f,
                    steps = 8
                )

                Spacer(Modifier.height(16.dp))

                Text("Genres", style = MaterialTheme.typography.titleMedium)
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    maxItemsInEachRow = 10,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    genres.forEach { genre ->
                        FilterChip(
                            selected = genre.id in selectedGenres,
                            onClick = { onGenreToggle(genre.id) },
                            label = { Text(genre.name) }
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(onClick = onReset) {
                        Text("Reset")
                    }
                    Button(onClick = onApply) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownSelector(
    label: String,
    modifier: Modifier = Modifier,
    selected: T?,
    items: List<T>,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String = { it.toString() },
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            value = selected?.let { itemLabel(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(itemLabel(item)) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}