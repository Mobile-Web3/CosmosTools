package com.mobileweb3.cosmostools.network.response

@kotlinx.serialization.Serializable
class AccountCreateResponse(
    val addresses: List<String>,
    val key: String
)