package com.waiyanphyo.astrofriday.data.repository

import com.waiyanphyo.astrofriday.domain.model.Astronomy
import com.waiyanphyo.astrofriday.domain.util.Error
import com.waiyanphyo.astrofriday.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface AstronomyRepository {
    fun getAstronomy(query: String, date: String): Flow<Result<Astronomy, Error>>
}