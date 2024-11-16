package com.waiyanphyo.astrofriday.domain.model

import java.time.Duration
import java.time.LocalDateTime

data class Astronomy(
    val locationName: String,
    val region: String,
    val country: String,
    val distance: Double,
    val localTime: String,
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    val sunriseToMoonrise: Duration,
    val sunsetToMoonset: Duration
)