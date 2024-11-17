package com.waiyanphyo.astrofriday.data.mappers

import com.waiyanphyo.astrofriday.data.model.ApiSearchLocation
import com.waiyanphyo.astrofriday.domain.model.SearchLocation

fun ApiSearchLocation.toDomainModel(): SearchLocation {
    return SearchLocation(
        id = id,
        name = name,
        region = region,
        country = country,
        latitude = lat,
        longitude = lon
    )
}

fun List<ApiSearchLocation>.toDomainModel(): List<SearchLocation> {
    return map { it.toDomainModel() }
}