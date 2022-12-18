package com.mobileweb3.cosmostools.network

@kotlinx.serialization.Serializable
data class BaseResponse<T>(
    val error: String,
    val isSuccess: Boolean,
    val result: T
)