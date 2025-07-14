package ru.stanise.animebrowsing.ui.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchModel : ViewModel() {
    private val _filters = MutableStateFlow(SearchUiState())
    val filters = _filters.asStateFlow()


    fun resetFilters(): SearchUiState {
        _filters.value = SearchUiState(selectedStatus = emptySet())
        return _filters.value
    }

    fun searchByQuery(query: String): SearchUiState {
        _filters.value = SearchUiState(query = query, selectedStatus = emptySet())
        return _filters.value
    }

    fun searchByFilters(searchState: SearchUiState): SearchUiState {
        _filters.value = searchState
        return _filters.value
    }
}