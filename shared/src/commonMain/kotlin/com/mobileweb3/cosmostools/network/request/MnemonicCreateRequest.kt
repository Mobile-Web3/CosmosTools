package com.mobileweb3.cosmostools.network.request

@kotlinx.serialization.Serializable
data class MnemonicCreateRequest(
    val mnemonicSize: Int
)