package com.waiyanphyo.astrofriday.data.model

data class ApiSearchLocation(
    val country: String,
    val id: Int,
    val lat: Double,
    val lon: Double,
    val name: String,
    val region: String,
    val url: String
)