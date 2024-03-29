package com.mobileweb3.cosmostools.wallet

import com.mobileweb3.cosmostools.core.entity.Account
import com.mobileweb3.cosmostools.crypto.buildMnemonic
import com.mobileweb3.cosmostools.network.Api
import com.mobileweb3.cosmostools.network.request.AccountCreateRequest
import com.mobileweb3.cosmostools.network.request.AccountRestoreRequest
import com.mobileweb3.cosmostools.network.request.MnemonicCreateRequest
import com.mobileweb3.cosmostools.network.response.AccountCreateResponse
import com.mobileweb3.cosmostools.network.response.AccountRestoreResponse
import com.mobileweb3.cosmostools.network.response.GetBalanceResponse
import com.mobileweb3.cosmostools.network.response.NetworkResponse
import com.mobileweb3.cosmostools.network.safeCall
import com.mobileweb3.cosmostools.repository.BalancesRepository
import com.mobileweb3.cosmostools.repository.NetworksRepository

class WalletInteractor internal constructor(
    private val walletStorage: WalletStorage,
    private val api: Api,
    private val balancesRepository: BalancesRepository,
    private val networksRepository: NetworksRepository
) {

    fun userHasPin(): Boolean = !getPinCode().isNullOrEmpty()

    fun getPinCode(): String? = walletStorage.userPin

    fun savePinCode(pinCode: String) {
        walletStorage.userPin = pinCode
    }

    suspend fun getNetworks() = networksRepository.getNetworks()

    suspend fun getCurrentNetwork(): NetworkResponse? =
        networksRepository.getNetworks().fold(
            onSuccess = { listOfNetworks ->
                listOfNetworks?.find {
                    it.prettyName == (walletStorage.currentNetwork ?: "Cosmos Hub")
                }
            },
            onFailure = { null }
        )

    fun setCurrentNetwork(network: NetworkResponse) {
        walletStorage.currentNetwork = network.prettyName
    }

    fun getMnemonicCounter(): Int = walletStorage.mnemonicCounter

    fun getPrivateKeyCounter(): Int = walletStorage.privateKeyCounter

    fun getIdForNewAccount(): Long = walletStorage.nextAccountId

    fun getAllAccounts(): List<Account> {
        return walletStorage.getAllAccounts()
    }

    fun getAllAccountsByNetwork(network: NetworkResponse): List<Account> {
        return walletStorage.getAllAccounts().filter { it.network == network.prettyName }
    }

    suspend fun getSelectedAccount(network: NetworkResponse? = null): Account? {
        return walletStorage
            .getAllAccounts()
            .find {
                it.id == walletStorage.getSelectedAccountInNetwork(
                    network ?: getCurrentNetwork()
                )
            }
    }

    fun setSelectedAccount(accountId: Long, network: NetworkResponse) {
        walletStorage.setSelectedAccountInNetwork(network, accountId)
    }

    suspend fun saveAccount(account: Account, network: NetworkResponse) {
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

    suspend fun getAccountBalance(address: String?): Result<GetBalanceResponse?> {
        return balancesRepository.getBalance(address, getCurrentNetwork()!!.chainId)
    }

    suspend fun getNewMnemonic(): Result<String> {
        return safeCall { api.createMnemonic(MnemonicCreateRequest(MNEMONIC_SIZE)) }
    }

    suspend fun createAddresses(
        displayedNetworks: List<NetworkResponse>,
        mnemonic: List<String>,
        hdPath: Int
    ): Result<AccountCreateResponse> {
        return safeCall {
            api.createAddresses(
                AccountCreateRequest(
                    accountPath = 0,
                    chainPrefixes = displayedNetworks.map { it.bech32Prefix },
                    coinType = 118,
                    indexPath = hdPath,
                    mnemonic = mnemonic.buildMnemonic()
                )
            )
        }
    }

    suspend fun restoreAddresses(
        displayedNetworks: List<NetworkResponse>,
        key: String
    ): Result<AccountRestoreResponse> {
        return safeCall {
            api.restoreAddresses(
                AccountRestoreRequest(
                    chainPrefixes = displayedNetworks.map { it.bech32Prefix },
                    key = key
                )
            )
        }
    }

    companion object {
        private const val MNEMONIC_SIZE = 24
    }
}