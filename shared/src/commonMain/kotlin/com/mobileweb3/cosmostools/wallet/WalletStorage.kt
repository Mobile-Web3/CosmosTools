package com.mobileweb3.cosmostools.wallet

import com.mobileweb3.cosmostools.core.entity.Account
import com.mobileweb3.cosmostools.network.response.NetworkResponse
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class WalletStorage(
    private val settings: Settings,
    private val json: Json
) {

    var userPin: String?
        get() {
            return settings.getStringOrNull(USER_PIN)
        }
        set(wallet) {
            settings[USER_PIN] = wallet
        }

    var currentNetwork: String?
        get() {
            return settings.getStringOrNull(CURRENT_NETWORK)
        }
        set(wallet) {
            settings[CURRENT_NETWORK] = wallet
        }

    val mnemonicCounter: Int
        get() {
            val mnemonicCounter = settings.getInt(MNEMONIC_COUNTER, 1)
            settings.putInt(MNEMONIC_COUNTER, mnemonicCounter + 1)
            return mnemonicCounter
        }

    val privateKeyCounter: Int
        get() {
            val privateKeyCounter = settings.getInt(PRIVATE_KEY_COUNTER, 1)
            settings.putInt(PRIVATE_KEY_COUNTER, privateKeyCounter + 1)
            return privateKeyCounter
        }

    private var accountsDiskCache: Map<Long, Account>
        get() {
            return settings.getStringOrNull(KEY_ACCOUNT_CACHE)?.let { str ->
                json.decodeFromString(ListSerializer(Account.serializer()), str).associateBy { it.id }
            } ?: mutableMapOf()
        }
        set(value) {
            val list = value.map { it.value }
            settings[KEY_ACCOUNT_CACHE] =
                json.encodeToString(ListSerializer(Account.serializer()), list)
        }
    private val accountsMemCache: MutableMap<Long, Account> by lazy { accountsDiskCache.toMutableMap() }

    val nextAccountId: Long
        get() {
            val idFromSettings = settings.getLong(KEY_ACCOUNT_ID_CACHE, 0)
            settings.putLong(KEY_ACCOUNT_ID_CACHE, idFromSettings + 1)
            return idFromSettings
        }

    suspend fun getAccount(id: Long): Account? = accountsMemCache[id]

    suspend fun saveAccount(account: Account) {
        accountsMemCache[account.id] = account
        accountsDiskCache = accountsMemCache
    }

    suspend fun saveAccounts(accounts: List<Account>) {
        accounts.forEach {
            accountsMemCache[it.id] = it
        }

        accountsDiskCache = accountsMemCache
    }

    fun deleteAccount(id: Long) {
        accountsMemCache.remove(id)
        accountsDiskCache = accountsMemCache
    }

    fun getAllAccounts(): List<Account> = accountsMemCache.values.toList()

    fun getSelectedAccountInNetwork(network: NetworkResponse?): Long? {
        return settings.getLongOrNull(KEY_SELECTED_ACCOUNT_NETWORK_CACHE + ":" + network?.prettyName)
    }

    fun setSelectedAccountInNetwork(network: NetworkResponse, id: Long) {
        settings.putLong(KEY_SELECTED_ACCOUNT_NETWORK_CACHE + ":" + network.prettyName, id)
    }

    companion object {
        private const val USER_PIN = "USER_PIN_CODE"

        private const val CURRENT_NETWORK = "CURRENT_NETWORK"

        private const val MNEMONIC_COUNTER = "MNEMONIC_COUNTER"
        private const val PRIVATE_KEY_COUNTER = "PRIVATE_KEY_COUNTER"

        private const val KEY_ACCOUNT_CACHE = "KEY_ACCOUNT_CACHE"
        private const val KEY_ACCOUNT_ID_CACHE = "KEY_ACCOUNT_ID_CACHE"
        private const val KEY_SELECTED_ACCOUNT_NETWORK_CACHE = "KEY_SELECTED_ACCOUNT_NETWORK_CACHE"
    }
}