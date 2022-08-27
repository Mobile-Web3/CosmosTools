package com.mobileweb3.cosmostools.wallet

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.json.Json

class WalletStorage(
    private val settings: Settings,
    private val json: Json
) {

    var currentWallet: String?
        get() {
            return settings.getStringOrNull(CURRENT_WALLET_ADDRESS)
        }
        set(wallet) {
            settings[CURRENT_WALLET_ADDRESS] = wallet
        }

    var currentNetwork: String?
        get() {
            return settings.getStringOrNull(CURRENT_NETWORK)
        }
        set(wallet) {
            settings[CURRENT_NETWORK] = wallet
        }

    companion object {
        private const val CURRENT_WALLET_ADDRESS = "CURRENT_WALLET_ADDRESS"
        private const val CURRENT_NETWORK = "CURRENT_NETWORK"
    }
}