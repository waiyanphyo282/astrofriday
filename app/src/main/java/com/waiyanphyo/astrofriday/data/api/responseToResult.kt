package com.waiyanphyo.astrofriday.data.api

import com.waiyanphyo.astrofriday.domain.util.DomainError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import com.waiyanphyo.astrofriday.domain.util.Result

fun <T> responseToResult(response: Response<T>): Flow<Result<T, DomainError>> = flow {
    // Emit the Loading state before starting the API call
    emit(Result.Loading)

    try {
        // Check if the response is successful
        if (response.isSuccessful) {
            // If successful, emit Success
            val body = response.body()
            if (body != null) {
                emit(Result.Success(body))
            } else {
                // If the body is null, emit an Error state
                emit(Result.Error(DomainError("Response body is null")))
            }
        } else {
            // If the response is not successful, emit an Error state with error message
            emit(Result.Error(DomainError("Error: ${response.code()} ${response.message()}")))
        }
    } catch (e: Exception) {
        // If an exception occurs, emit an Error state
        emit(Result.Error(DomainError(e.message ?: "Unknown error")))
    }
}
