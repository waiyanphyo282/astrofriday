package com.waiyanphyo.astrofriday.data.api

import com.waiyanphyo.astrofriday.data.model.ApiSearchLocation
import com.waiyanphyo.astrofriday.data.model.ApiSportList
import com.waiyanphyo.astrofriday.data.model.AstronomyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("search.json")
    suspend fun searchLocation(@Query("q") query: String): Response<List<ApiSearchLocation>>

    @GET("sports.json")
    suspend fun getSports(@Query("q") query: String): Response<ApiSportList>

    @GET("astronomy.json")
    suspend fun getAstronomy(@Query("q") query: String, @Query("dt") date: String): Response<AstronomyResponse>
}