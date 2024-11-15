package com.waiyanphyo.astrofriday.data.model

data class ApiSportList(
    val cricket: List<ApiSport>,
    val football: List<ApiSport>,
    val golf: List<ApiSport>
)