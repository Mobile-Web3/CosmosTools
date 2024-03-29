package com.mobileweb3.cosmostools.network.request

import kotlinx.serialization.Serializable

@Serializable
data class GetBalanceRequest(
    val address: String,
    val chainId: String
)