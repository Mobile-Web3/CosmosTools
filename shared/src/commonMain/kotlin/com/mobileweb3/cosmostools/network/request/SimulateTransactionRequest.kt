package com.mobileweb3.cosmostools.network.request

import kotlinx.serialization.Serializable

@Serializable
data class SimulateTransactionRequest(
    val amount: String,
    val from: String,
    val memo: String,
    val key: String,
    val to: String,
    val chainId: String
)