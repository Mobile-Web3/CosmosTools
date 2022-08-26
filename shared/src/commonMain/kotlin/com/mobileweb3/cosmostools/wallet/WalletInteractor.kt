package com.mobileweb3.cosmostools.wallet

class WalletInteractor internal constructor(
    private val walletStorage: WalletStorage
) {

    fun getCurrentWallet(): String? = walletStorage.currentWallet

    companion object
}