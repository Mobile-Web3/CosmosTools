package com.mobileweb3.cosmostools.network.response

@kotlinx.serialization.Serializable
data class GetValidatorsResponse(
    val data: List<ValidatorResponse>,
    val limit: Int,
    val offset: Int
)

@kotlinx.serialization.Serializable
data class ValidatorResponse(
    val address: String,
    val commission: String,
    val description: String,
    val identity: String,
    val name: String,
    val tokens: String,
    val website: String
)