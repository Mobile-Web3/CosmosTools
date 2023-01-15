package com.mobileweb3.cosmostools.network.request

@kotlinx.serialization.Serializable
class AccountCreateRequest(
    val accountPath: Int,
    val chainPrefixes: List<String>,
    val coinType: Int,
    val indexPath: Int,
    val mnemonic: String
)