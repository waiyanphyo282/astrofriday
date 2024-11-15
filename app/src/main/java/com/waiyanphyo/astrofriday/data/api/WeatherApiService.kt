package com.waiyanphyo.astrofriday.data.api

import com.waiyanphyo.astrofriday.data.model.ApiLocation
import com.waiyanphyo.astrofriday.data.model.ApiSportList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("search.json")
    suspend fun searchLocation(@Query("q") query: String): Response<List<ApiLocation>>

    @GET("sports.json")
    suspend fun getSports(@Query("q") query: String): Response<ApiSportList>
}