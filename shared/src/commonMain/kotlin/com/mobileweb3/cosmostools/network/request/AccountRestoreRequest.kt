package com.mobileweb3.cosmostools.network.request

@kotlinx.serialization.Serializable
class AccountRestoreRequest(
    val chainPrefixes: List<String>,
    val key: String
)