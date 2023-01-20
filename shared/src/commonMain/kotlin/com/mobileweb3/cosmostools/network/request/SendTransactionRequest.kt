package com.mobileweb3.cosmostools.network.request

import kotlinx.serialization.Serializable

@Serializable
data class SendTransactionRequest(
    val amount: String,
    val chainId: String,
    val from: String,
    val gasAdjusted: String,
    val gasPrice: String,
    val key: String,
    val memo: String,
    val to: String,
)

@Serializable
data class SendTransactionRequestWithFirebase(
    val amount: String,
    val chainId: String,
    val from: String,
    val gasAdjusted: String,
    val gasPrice: String,
    val key: String,
    val memo: String,
    val to: String,
    val firebaseToken: String
)