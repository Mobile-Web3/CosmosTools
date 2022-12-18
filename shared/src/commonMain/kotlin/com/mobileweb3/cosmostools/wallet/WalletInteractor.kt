package com.mobileweb3.cosmostools.wallet

import com.mobileweb3.cosmostools.core.entity.Account
import com.mobileweb3.cosmostools.crypto.Network
import com.mobileweb3.cosmostools.crypto.mockNetworks
import com.mobileweb3.cosmostools.network.Api
import com.mobileweb3.cosmostools.network.request.GetBalanceRequest
import com.mobileweb3.cosmostools.network.response.GetBalanceResponse
import com.mobileweb3.cosmostools.network.safeCall

class WalletInteractor internal constructor(
    private val walletStorage: WalletStorage,
    private val api: Api
) {

    fun userHasPin(): Boolean = !getPinCode().isNullOrEmpty()

    fun getPinCode(): String? = walletStorage.userPin

    fun savePinCode(pinCode: String) {
        walletStorage.userPin = pinCode
    }

    fun getCurrentNetwork(): Network =
        mockNetworks.find { it.pretty_name == (walletStorage.currentNetwork ?: "Cosmos Hub") }!!

    fun setCurrentNetwork(network: Network) {
        walletStorage.currentNetwork = network.pretty_name
    }

    fun getMnemonicCounter(): Int = walletStorage.mnemonicCounter

    fun getPrivateKeyCounter(): Int = walletStorage.privateKeyCounter

    fun getIdForNewAccount(): Long = walletStorage.nextAccountId

    fun getAllAccounts(): List<Account> {
        return walletStorage.getAllAccounts()
    }

    fun getAllAccountsByNetwork(network: Network): List<Account> {
        return walletStorage.getAllAccounts().filter { it.network == network.pretty_name }
    }

    fun getSelectedAccount(network: Network? = null): Account? {
        return walletStorage
            .getAllAccounts()
            .find { it.id == walletStorage.getSelectedAccountInNetwork(network ?: getCurrentNetwork()) }
    }

    fun setSelectedAccount(accountId: Long, network: Network) {
        walletStorage.setSelectedAccountInNetwork(network, accountId)
    }

    suspend fun saveAccount(account: Account, network: Network) {
        if (getCurrentNetwork() == network) {
            walletStorage.setSelectedAccountInNetwork(network, account.id)
        }
        walletStorage.saveAccount(account)
    }

    fun deleteAccount(id: Long) = walletStorage.deleteAccount(id)

    fun deleteAccountsBySource(sourceTitle: String?) {
        walletStorage
            .getAllAccounts()
            .filter { account -> account.sourceTitle == sourceTitle }
            .forEach { account ->
                deleteAccount(account.id)
            }
    }

    suspend fun getAccountBalance(address: String): Result<GetBalanceResponse> {
        return safeCall {
            api.getBalance(GetBalanceRequest(address))
        }
    }

    companion object
}