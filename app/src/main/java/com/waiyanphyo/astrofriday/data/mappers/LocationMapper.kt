package com.waiyanphyo.astrofriday.data.mappers

import com.waiyanphyo.astrofriday.data.model.ApiLocation
import com.waiyanphyo.astrofriday.domain.model.Location

fun ApiLocation.toDomainModel(): Location {
    return Location(
        id = id,
        name = name,
        region = region,
        country = country,
        latitude = lat,
        longitude = lon
    )
}