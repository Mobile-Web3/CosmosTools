package com.mobileweb3.cosmostools.crypto

data class Network(
    val chain_name: String,
    val status: String,
    val website: String? = null,
    val pretty_name: String,
    val bech32_prefix: String,
    val key_algos: List<String>,
    val slip44: Int,
    val fees: NetworkFees? = null,
    val staking: NetworkStaking? = null,
    val logo_URIs: NetworkLogos? = null,
    val explorers: List<NetworkExplorer>? = null,
    val assets: List<NetworkAsset>
) {

    fun getLogo(): String? {
        return assets[0].logo_URIs?.png ?: assets[0].logo_URIs?.svg//logo_URIs?.png ?: logo_URIs?.svg
    }

    fun getAddressExplorerLink(address: String?): String {
        return explorers?.first { it.account_page != null }?.account_page + "/" + address
    }
}

data class NetworkFees(
    val fee_tokens: List<NetworkToken>? = null
)

data class NetworkStaking(
    val staking_tokens: List<NetworkToken>? = null
)

data class NetworkToken(
    val denom: String,
    val fixed_min_gas_price: Double? = null,
    val low_gas_price: Double? = null,
    val average_gas_price: Double? = null,
    val high_gas_price: Double? = null,
    val exponent: Int? = null
)

data class NetworkLogos(
    val png: String? = null,
    val svg: String? = null
)

data class NetworkExplorer(
    val kind: String, //mintscan
    val url: String, //https://www.mintscan.io/osmosis
    val tx_page: String? = null, //https://www.mintscan.io/osmosis/txs/${txHash}
    val account_page: String? = null //https://www.mintscan.io/osmosis/account/${accountAddress}
)

data class NetworkAsset(
    val description: String? = null,
    val denom_units: List<NetworkToken>,
    val base: String,
    val name: String,
    val display: String,
    val symbol: String,
    val logo_URIs: NetworkLogos?,
    val coingecko_id: String
)