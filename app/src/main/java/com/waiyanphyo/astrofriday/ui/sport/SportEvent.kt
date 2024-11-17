package com.waiyanphyo.astrofriday.ui.sport

sealed interface SportEvent {
    data class Error(val message: String) : SportEvent
}