package com.mobileweb3.cosmostools.network.response

import kotlinx.serialization.Serializable

@Serializable
data class SendTransactionResponse(
    val txHash: String,
    val withEvents: Boolean = false
)