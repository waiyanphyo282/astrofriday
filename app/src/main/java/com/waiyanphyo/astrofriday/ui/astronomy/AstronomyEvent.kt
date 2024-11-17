package com.waiyanphyo.astrofriday.ui.astronomy

sealed interface AstronomyEvent {
    data class Error(val message: String) : AstronomyEvent
}