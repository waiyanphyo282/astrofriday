package com.waiyanphyo.astrofriday.ui.search

import com.waiyanphyo.astrofriday.domain.model.Location
import javax.annotation.concurrent.Immutable


@Immutable
data class SearchUiState(
    val isLoading: Boolean = false,
    val locations: List<Location>? = null,
    val errorMessage: String = ""
)
