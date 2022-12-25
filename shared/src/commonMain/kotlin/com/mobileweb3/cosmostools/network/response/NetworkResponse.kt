package com.mobileweb3.cosmostools.network.response

@kotlinx.serialization.Serializable
data class NetworkResponse(
    val base: String,
    val bech32Prefix: String,
    val chainId: String,
    val chainName: String,
    val description: String,
    val display: String,
    val logoPngUrl: String,
    val logoSvgUrl: String,
    val prettyName: String,
    val slip44: Int,
    val symbol: String,
    val keyAlgos: List<String>? = null
) {

    fun getLogo(): String {
        return when {
            logoPngUrl.isNotEmpty() -> logoPngUrl
            logoSvgUrl.isNotEmpty() -> logoSvgUrl
            else -> ""
        }
    }
}