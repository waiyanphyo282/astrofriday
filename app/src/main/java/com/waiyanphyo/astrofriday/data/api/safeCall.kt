package com.waiyanphyo.astrofriday.data.api

import com.waiyanphyo.astrofriday.domain.util.NetworkError
import com.waiyanphyo.astrofriday.domain.util.Result
import kotlinx.coroutines.ensureActive
import retrofit2.Response
import java.net.UnknownHostException
import java.nio.channels.UnresolvedAddressException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: suspend () -> Response<T>
): Result<T, NetworkError> {
    val response = try {
        execute()  // Perform the network request
    } catch (e: UnknownHostException) {
        return Result.Error(NetworkError.NO_INTERNET)  // Handle no internet case
    } catch (e: UnresolvedAddressException) {
        return Result.Error(NetworkError.NO_INTERNET)  // Handle no internet case
    } catch (e: Exception) {
        coroutineContext.ensureActive()  // Ensure the coroutine is still active
        return Result.Error(NetworkError.UNKNOWN)  // Handle all other unknown errors
    }

    // Process the response and map it to the Result
    return responseToResult(response)
}