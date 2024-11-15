package com.waiyanphyo.astrofriday.ui.sport

import com.waiyanphyo.astrofriday.domain.model.AllSports
import javax.annotation.concurrent.Immutable

@Immutable
data class SportUiState(
    val isLoading: Boolean = false,
    val allSports: AllSports? = null
)
