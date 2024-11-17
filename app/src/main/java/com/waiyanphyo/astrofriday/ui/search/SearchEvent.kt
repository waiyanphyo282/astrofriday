package com.waiyanphyo.astrofriday.ui.search

sealed interface SearchEvent {
    data class ErrorTextView(val message: String) : SearchEvent
    data class ErrorToast(val message: String) : SearchEvent
}