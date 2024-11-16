package com.waiyanphyo.astrofriday.domain.model

import java.time.Duration
import java.time.LocalDateTime

data class Astronomy(
    val locationName: String,
    val region: String,
    val country: String,
    val distance: Double,
    val localTime: String,
    val sunrise: LocalDateTime,
    val sunset: LocalDateTime,
    val moonrise: LocalDateTime,
    val moonset: LocalDateTime,
    val sunriseToMoonrise: Duration,
    val sunsetToMoonset: Duration
)