package com.mobileweb3.cosmostools.network.request

import kotlinx.serialization.Serializable

@Serializable
data class SendTransactionRequest(
    val amount: String,
    val from: String,
    val memo: String,
    val mnemonic: String,
    val to: String
)