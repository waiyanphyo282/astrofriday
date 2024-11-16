package com.waiyanphyo.astrofriday.data.mappers

import com.waiyanphyo.astrofriday.data.model.AstronomyResponse
import com.waiyanphyo.astrofriday.domain.model.Astronomy
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

private const val LOCAL_TIME_FORMAT = "yyyy-MM-dd HH:mm"
private const val TARGET_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

fun AstronomyResponse.toDomainModel(): Astronomy {
    val localDateTime = LocalDateTime.parse(location.localtime, DateTimeFormatter.ofPattern(LOCAL_TIME_FORMAT))
    val localDate = localDateTime.toLocalDate()
    val zoneOffset = ZoneOffset.UTC
    val sunsetDateTime = LocalDateTime.of(localDate, parseTimeOfDay(astronomy.astro.sunset)).atOffset(zoneOffset).toZonedDateTime()
    val moonsetDateTime = if (astronomy.astro.moonset == "No moonset") {
        null
    } else {
        LocalDateTime.of(localDate, parseTimeOfDay(astronomy.astro.moonset)).atOffset(zoneOffset).toZonedDateTime()
    }

    // If moonset time is earlier than sunset, consider it as happening the next day
    val correctedMoonsetDateTime = if (moonsetDateTime?.isBefore(sunsetDateTime) == true) {
        moonsetDateTime.plusDays(1)  // Adjust to the next day
    } else {
        moonsetDateTime
    }

    val sunriseTime = parseTimeOfDay(astronomy.astro.sunrise)
    val moonriseTime = parseTimeOfDay(astronomy.astro.moonrise)

    val formattedLocalTime = formatLocalTime(location.localtime)

    return Astronomy(
        locationName = location.name,
        region = location.region,
        country = location.country,
        localTime = formattedLocalTime,
        distance = getDistanceInMeters(
            location.lat,
            location.lon,
            13.7461252,100.530772,
        ),
        sunrise = astronomy.astro.sunrise,
        sunset = astronomy.astro.sunset,
        moonrise = astronomy.astro.moonrise,
        moonset = astronomy.astro.moonset,
        sunriseToMoonrise = if (sunriseTime != null && moonriseTime != null) Duration.between(sunriseTime, moonriseTime) else Duration.ZERO,
        sunsetToMoonset = if (sunsetDateTime != null && moonsetDateTime != null) Duration.between(sunsetDateTime, correctedMoonsetDateTime) else Duration.ZERO,
    )
}

private fun formatLocalTime(localTimeString: String): String {
    val localDateTime = LocalDateTime.parse(localTimeString, DateTimeFormatter.ofPattern(LOCAL_TIME_FORMAT))
    val zonedDateTime = localDateTime.atOffset(ZoneOffset.UTC).toZonedDateTime()
    val targetFormatter = DateTimeFormatter.ofPattern(TARGET_TIME_FORMAT)
    return zonedDateTime.format(targetFormatter)
}


fun getDistanceInMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val earthRadius = 6371000.0 // in meters
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return earthRadius * c
}


private fun parseTimeOfDay(timeString: String): LocalTime? {
    if (timeString.isNullOrEmpty() || timeString == "No moonset" || timeString == "No sunrise" || timeString == "No sunset") {
        return null  // Return null if the value is empty or represents no data
    }
    val (timePart, amPmPart) = timeString.split(" ")
    val (hours, minutes) = timePart.split(":")
    val hour = hours.toInt()
    val minute = minutes.toInt()
    return when (amPmPart) {
        "AM" -> LocalTime.of(hour, minute)
        "PM" -> LocalTime.of(if (hour < 12) hour + 12 else hour, minute)
        else -> throw IllegalArgumentException("Invalid time of day format: $timeString")
    }
}