package com.waiyanphyo.astrofriday.data.repository

import com.waiyanphyo.astrofriday.domain.model.SearchLocation
import com.waiyanphyo.astrofriday.domain.util.Error
import com.waiyanphyo.astrofriday.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchLocation(query: String): Flow<Result<List<SearchLocation>, Error>>
}