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
    val sunriseTime = parseTimeOfDay(astronomy.astro.sunrise)
    val sunsetTime = parseTimeOfDay(astronomy.astro.sunset)
    val moonriseTime = parseTimeOfDay(astronomy.astro.moonrise)
    val moonsetTime = parseTimeOfDay(astronomy.astro.moonset)

    val formattedLocalTime = formatLocalTime(location.localtime)

    val localDateTime = LocalDateTime.parse(location.localtime, DateTimeFormatter.ofPattern(LOCAL_TIME_FORMAT))
    val localDate = localDateTime.toLocalDate()

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
        sunrise = LocalDateTime.of(localDate, sunriseTime),
        sunset = LocalDateTime.of(localDate, sunsetTime),
        moonrise = LocalDateTime.of(localDate, moonriseTime),
        moonset = LocalDateTime.of(localDate, moonsetTime),
        sunriseToMoonrise = Duration.between(sunriseTime, moonriseTime),
        sunsetToMoonset = Duration.between(sunsetTime, moonsetTime),
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


private fun parseTimeOfDay(timeString: String): LocalTime {
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