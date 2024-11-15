package com.waiyanphyo.astrofriday.data.repository

import com.waiyanphyo.astrofriday.domain.model.AllSports
import com.waiyanphyo.astrofriday.domain.util.DomainError
import com.waiyanphyo.astrofriday.domain.util.Error
import com.waiyanphyo.astrofriday.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface SportRepository {

    fun getSports(query: String): Flow<Result<AllSports, Error>>

}