package com.waiyanphyo.astrofriday.data.repository

import com.waiyanphyo.astrofriday.domain.model.Location
import com.waiyanphyo.astrofriday.domain.util.DomainError
import com.waiyanphyo.astrofriday.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchLocation(query: String): Flow<Result<List<Location>, DomainError>>
}