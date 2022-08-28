package com.mobileweb3.cosmostools.crypto

expect object Address {

    fun createAddressFromEntropyByNetwork(
        network: Network,
        entropy: String,
        path: Int,
        customPath: Int
    ): String

    fun getDpAddress(
        network: Network,
        pubHex: String
    ): String
}