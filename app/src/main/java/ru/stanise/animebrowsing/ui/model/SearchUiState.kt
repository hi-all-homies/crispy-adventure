package ru.stanise.animebrowsing.ui.model

import ru.stanise.animebrowsing.type.AnimeKindEnum
import ru.stanise.animebrowsing.type.AnimeStatusEnum

data class SearchUiState(
    val query: String = "",
    val selectedKind: AnimeKindEnum? = null,
    val selectedStatus: AnimeStatusEnum? = AnimeStatusEnum.ongoing,
    val minScore: Float = 0f,
    val selectedGenres: Set<String> = emptySet(),
    val page: Int = 1
)
