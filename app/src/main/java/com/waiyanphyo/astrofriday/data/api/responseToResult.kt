package com.waiyanphyo.astrofriday.data.api

import android.util.Log
import com.squareup.moshi.JsonDataException
import com.waiyanphyo.astrofriday.domain.util.NetworkError
import com.waiyanphyo.astrofriday.domain.util.Result
import retrofit2.Response

fun <T> responseToResult(response: Response<T>): Result<T, NetworkError> {
    Log.d("TAG", "responseToResult: ${response.code()}")
    return when {
        response.isSuccessful -> {
            // Handle the successful response
            try {
                response.body()?.let {body ->
                    Result.Success(body)
                } ?: Result.Error(NetworkError.UNKNOWN)
            } catch (e: JsonDataException) {
                Result.Error(NetworkError.SERIALIZATION)
            }
        }
        response.code() == 408 -> {
            // Handle 404 Not Found error
            Result.Error(NetworkError.REQUEST_TIMEOUT)
        }
        response.code() == 429 -> {
            // Handle 401 Unauthorized error
            Result.Error(NetworkError.TOO_MANY_REQUESTS)
        }
        response.code() in 500..599 -> {
            // Handle 5xx Server error
            Result.Error(NetworkError.SERVER_ERROR)
        }
        else -> {
            // Handle other HTTP errors
            Result.Error(NetworkError.UNKNOWN)
        }
    }
}