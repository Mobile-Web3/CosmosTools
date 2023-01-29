package com.mobileweb3.cosmostools.network.request

@kotlinx.serialization.Serializable
data class GetValidatorsRequest(
    val chainId: String,
    val limit: Int,
    val offset: Int
)