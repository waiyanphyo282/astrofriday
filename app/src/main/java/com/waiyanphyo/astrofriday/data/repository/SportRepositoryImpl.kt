package com.waiyanphyo.astrofriday.data.repository

import com.waiyanphyo.astrofriday.data.api.WeatherApiService
import com.waiyanphyo.astrofriday.data.api.responseToResult
import com.waiyanphyo.astrofriday.data.mappers.toDomainModel
import com.waiyanphyo.astrofriday.di.IoDispatcher
import com.waiyanphyo.astrofriday.domain.model.AllSports
import com.waiyanphyo.astrofriday.domain.util.DomainError
import com.waiyanphyo.astrofriday.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SportRepositoryImpl @Inject constructor(
    private val apiService: WeatherApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): SportRepository  {


    override fun getSports(query: String): Flow<Result<AllSports, DomainError>> = flow {
        val response = apiService.getSports(query)
        emitAll(responseToResult(response).map {result ->
            when (result) {
                is Result.Success -> Result.Success(result.data.toDomainModel())
                is Result.Error -> Result.Error(result.error)
                is Result.Loading -> Result.Loading
                Result.Idle -> Result.Idle
            }
        })
    }.flowOn(ioDispatcher)

}