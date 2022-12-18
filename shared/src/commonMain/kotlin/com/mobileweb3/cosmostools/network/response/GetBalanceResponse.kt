package com.mobileweb3.cosmostools.network.response

import kotlinx.serialization.Serializable

@Serializable
data class GetBalanceResponse(
    val availableAmount: String,
    val stakedAmount: String,
    val totalAmount: String,
)