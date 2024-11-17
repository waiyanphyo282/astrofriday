package com.waiyanphyo.astrofriday.data.mappers

import com.waiyanphyo.astrofriday.data.model.ApiSport
import com.waiyanphyo.astrofriday.data.model.ApiSportList
import com.waiyanphyo.astrofriday.domain.model.AllSports
import com.waiyanphyo.astrofriday.domain.model.Sport

fun ApiSport.toDomainModel(): Sport {
    val date = start.split(" ")[0].replace("-", "/")
    return Sport(
        stadium = stadium,
        country = country,
        tournament = tournament,
        date = date,
        match = match
    )
}

fun ApiSportList.toDomainModel() =
    AllSports(
        cricket = cricket.map { it.toDomainModel() },
        football = football.map { it.toDomainModel() },
        golf = golf.map { it.toDomainModel() }
    )