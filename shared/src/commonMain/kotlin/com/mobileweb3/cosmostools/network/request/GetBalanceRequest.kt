package com.mobileweb3.cosmostools.network.request

import kotlinx.serialization.Serializable

@Serializable
data class GetBalanceRequest(
    val walletAddress: String
)