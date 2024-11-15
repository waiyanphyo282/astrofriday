package com.waiyanphyo.astrofriday.data.api

import com.waiyanphyo.astrofriday.data.model.ApiLocation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("search.json")
    suspend fun searchLocation(@Query("q") query: String): Response<List<ApiLocation>>
}