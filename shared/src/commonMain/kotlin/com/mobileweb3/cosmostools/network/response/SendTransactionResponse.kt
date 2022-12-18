package com.mobileweb3.cosmostools.network.response

import kotlinx.serialization.Serializable

@Serializable
data class SendTransactionResponse(
    val data: String,
    val gasUsed: Int,
    val gasWanted: Int,
    val height: Int,
    val rawLog: String,
    val txHash: String
)