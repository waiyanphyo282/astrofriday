package com.waiyanphyo.astrofriday.data.repository

import com.waiyanphyo.astrofriday.data.api.WeatherApiService
import com.waiyanphyo.astrofriday.data.api.safeCall
import com.waiyanphyo.astrofriday.data.mappers.toDomainModel
import com.waiyanphyo.astrofriday.data.model.AstronomyResponse
import com.waiyanphyo.astrofriday.di.IoDispatcher
import com.waiyanphyo.astrofriday.domain.util.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AstronomyRepositoryImpl @Inject constructor(
    private val apiService: WeatherApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): AstronomyRepository {

    override fun getAstronomy(query: String) = flow {
        val response = safeCall { apiService.getAstronomy(query) }
        emit(response.map { it.toDomainModel() })
    }.flowOn(ioDispatcher)
}