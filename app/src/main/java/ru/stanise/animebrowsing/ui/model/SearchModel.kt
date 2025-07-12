package ru.stanise.animebrowsing.ui.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.stanise.animebrowsing.type.AnimeKindEnum
import ru.stanise.animebrowsing.type.AnimeStatusEnum

class SearchModel : ViewModel() {
    private val _filters = MutableStateFlow(SearchUiState())
    val filters = _filters.asStateFlow()

    fun updateQuery(newQuery: String) {
        _filters.update { it.copy(query = newQuery) }
    }

    fun updateKind(kind: AnimeKindEnum?) {
        _filters.update { it.copy(selectedKind = kind) }
    }

    fun updateStatus(status: AnimeStatusEnum?) {
        _filters.update { it.copy(selectedStatus = status) }
    }

    fun updateScore(score: Float) {
        _filters.update { it.copy(minScore = score) }
    }

    fun toggleGenre(genreId: String) {
        _filters.update {
            val current = it.selectedGenres
            val updated = if (current.contains(genreId)) current - genreId else current + genreId
            it.copy(selectedGenres = updated)
        }
    }

    fun resetFilters() {
        _filters.value = SearchUiState(selectedStatus = null)
    }

    fun applyFilters(): SearchUiState {
        return _filters.value
    }
}