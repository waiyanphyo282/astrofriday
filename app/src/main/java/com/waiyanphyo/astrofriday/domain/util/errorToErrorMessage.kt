package com.waiyanphyo.astrofriday.domain.util

fun Error.toErrorMessage(): String {
    return when (this) {
        is NetworkError -> {
            when (this) {
                NetworkError.NO_INTERNET -> "No internet connection"
                NetworkError.REQUEST_TIMEOUT -> "Request timeout"
                NetworkError.TOO_MANY_REQUESTS -> "Too many requests"
                NetworkError.SERVER_ERROR -> "Server error"
                NetworkError.SERIALIZATION -> "Serialization error"
                NetworkError.UNKNOWN -> "Unknown error"
            }
        }
        else -> "Unknown error"
    }
}