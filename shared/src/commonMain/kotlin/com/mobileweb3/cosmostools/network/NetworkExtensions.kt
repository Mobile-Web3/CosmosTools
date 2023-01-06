package com.mobileweb3.cosmostools.network

import io.ktor.utils.io.*

suspend fun <T> safeCall(
    block: suspend () -> BaseResponse<T>,
): Result<T> {
    return runCatching {
        block()
    }.fold(
        onSuccess = { callResult ->
            if (callResult.isSuccess && callResult.result != null) {
                Result.success(callResult.result)
            } else {
                Result.failure(Exception(callResult.error))
            }
        },
        onFailure = { throwable ->
            if (throwable is CancellationException) {
                throw throwable
            }
            Result.failure(throwable)
        }
    )
}