package com.mobileweb3.cosmostools.wallet

import com.mobileweb3.cosmostools.core.entity.Account
import com.mobileweb3.cosmostools.crypto.Network
import com.mobileweb3.cosmostools.crypto.mockNetworks

class WalletInteractor internal constructor(
    private val walletStorage: WalletStorage
) {

    fun getCurrentAccount(): Account? = walletStorage.currentAccount

    fun setCurrentAccount(account: Account) {
        walletStorage.currentAccount = account
    }

    fun getCurrentNetwork(): Network =
        mockNetworks.find { it.pretty_name == (walletStorage.currentNetwork ?: "Cosmos Hub") }!!

    fun setCurrentNetwork(network: Network) {
        walletStorage.currentNetwork = network.pretty_name
    }

    fun getMnemonicCounter(): Int = walletStorage.mnemonicCounter

    fun getIdForNewAccount(): Long = walletStorage.nextAccountId

    suspend fun getAllAccounts(network: Network): List<Account> {
        return walletStorage.getAllAccounts().filter { it.network == network.pretty_name }
    }

    suspend fun getSelectedAccount(network: Network): Account? {
        return walletStorage
            .getAllAccounts()
            .find { it.id == walletStorage.getSelectedAccountInNetwork(network) }
    }

    suspend fun setSelectedAccount(accountId: Long, network: Network) {
        walletStorage.setSelectedAccountInNetwork(network, accountId)
    }

    suspend fun saveAccount(account: Account, network: Network) {
        if (walletStorage.getSelectedAccountInNetwork(network) == null) {
            walletStorage.setSelectedAccountInNetwork(network, account.id)
        }
        walletStorage.saveAccount(account)
    }

    suspend fun deleteAccount(id: Long) = walletStorage.deleteAccount(id)

    companion object
}