package ru.stanise.animebrowsing.ui.model

import androidx.compose.runtime.saveable.Saver
import ru.stanise.animebrowsing.dto.Status

data class SearchUiState(
    val query: String = "",
    val selectedKind: Set<String> = emptySet(),
    val selectedStatus: Set<String> = setOf(Status.AIRING.rawValue),
    val minScore: Float = 0f,
    val selectedGenres: Set<String> = emptySet(),
    val page: Int = 1
)

val SearchUiStateSaver: Saver<SearchUiState, *> = Saver(
    save = { state ->
        listOf(
            state.query,
            state.selectedKind.toList(),
            state.selectedStatus.toList(),
            state.minScore,
            state.selectedGenres.toList(),
            state.page
        )
    },
    restore = { list ->
        try {
            @Suppress("UNCHECKED_CAST")
            SearchUiState(
                query = list[0] as String,
                selectedKind = (list[1] as List<String>).toSet(),
                selectedStatus = (list[2] as List<String>).toSet(),
                minScore = list[3] as Float,
                selectedGenres = (list[4] as List<String>).toSet(),
                page = list[5] as Int
            )
        } catch (e: Exception) {
            e.printStackTrace()
            SearchUiState() // fallback to default
        }
    }
)
