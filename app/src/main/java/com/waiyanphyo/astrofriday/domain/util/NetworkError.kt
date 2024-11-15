package com.waiyanphyo.astrofriday.domain.util

enum class NetworkError: Error {
    REQUEST_TIMEOUT,
    TOO_MANY_REQUESTS,
    SERIALIZATION,
    SERVER_ERROR,
    NO_INTERNET,
    UNKNOWN,
}