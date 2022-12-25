package com.mobileweb3.cosmostools.crypto

import com.mobileweb3.cosmostools.network.response.NetworkResponse

expect object Address {

    fun createAddressFromEntropyByNetwork(
        network: NetworkResponse,
        entropy: String,
        path: Int,
        customPath: Int
    ): String

    fun getDpAddress(
        network: NetworkResponse,
        pubHex: String
    ): String
}