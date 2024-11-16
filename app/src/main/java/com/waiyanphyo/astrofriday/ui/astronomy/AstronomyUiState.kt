package com.waiyanphyo.astrofriday.ui.astronomy

import com.waiyanphyo.astrofriday.domain.model.Astronomy
import javax.annotation.concurrent.Immutable

@Immutable
data class AstronomyUiState(
    val isLoading: Boolean = false,
    val astronomy: Astronomy? = null,
)