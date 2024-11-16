package com.waiyanphyo.astrofriday.ui.search

import com.waiyanphyo.astrofriday.domain.model.SearchLocation
import javax.annotation.concurrent.Immutable


@Immutable
data class SearchUiState(
    val isLoading: Boolean = false,
    val locations: List<SearchLocation>? = null,
    val errorMessage: String = ""
)
