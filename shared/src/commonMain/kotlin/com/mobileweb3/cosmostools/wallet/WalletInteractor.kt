package com.mobileweb3.cosmostools.wallet

import com.mobileweb3.cosmostools.crypto.Network
import com.mobileweb3.cosmostools.crypto.mockNetworks

class WalletInteractor internal constructor(
    private val walletStorage: WalletStorage
) {

    fun getCurrentWallet(): String? = walletStorage.currentWallet

    fun getCurrentNetwork(): Network =
        mockNetworks.find { it.pretty_name == (walletStorage.currentNetwork ?: "Cosmos Hub") }!!

    companion object
}