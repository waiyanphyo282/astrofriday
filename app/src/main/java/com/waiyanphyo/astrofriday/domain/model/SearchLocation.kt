package com.waiyanphyo.astrofriday.domain.model

data class SearchLocation(
    val id: Int,
    val name: String,
    val region: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
) {
    val fullName: String get() = "$name, $region, $country"
    fun formattedLocation(): String = "Latitude: $latitude, Longitude: $longitude"
}
