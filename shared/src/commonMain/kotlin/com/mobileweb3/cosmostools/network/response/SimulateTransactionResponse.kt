package com.mobileweb3.cosmostools.network.response

@kotlinx.serialization.Serializable
data class SimulateTransactionResponse(
    val averageGasPrice: String,
    val gasAdjusted: String,
    val highGasPrice: String,
    val lowGasPrice: String
)