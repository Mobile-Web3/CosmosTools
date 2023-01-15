package com.mobileweb3.cosmostools.network.response

@kotlinx.serialization.Serializable
class AccountRestoreResponse(
    val addresses: List<String>,
    val key: String
)