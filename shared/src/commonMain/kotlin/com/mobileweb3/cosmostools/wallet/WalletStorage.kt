package com.mobileweb3.cosmostools.wallet

import com.mobileweb3.cosmostools.core.entity.Account
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class WalletStorage(
    private val settings: Settings,
    private val json: Json
) {

    var currentAccount: Account?
        get() {
            return try {
                json.decodeFromString(Account.serializer(), settings.getStringOrNull(CURRENT_ACCOUNT) ?: "")
            } catch (ex: Exception) {
                return null
            }
        }
        set(account) {
            settings[CURRENT_ACCOUNT] = json.encodeToString(Account.serializer(), account!!)
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

    private var diskCache: Map<Long, Account>
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
    private val memCache: MutableMap<Long, Account> by lazy { diskCache.toMutableMap() }

    val nextAccountId: Long
        get() {
            val idFromSettings = settings.getLong(KEY_ACCOUNT_ID_CACHE, 0)
            settings.putLong(KEY_ACCOUNT_ID_CACHE, idFromSettings + 1)
            return idFromSettings
        }
    var activeAccountId: Long
        get() {
            return settings.getLong(KEY_LAST_ACCOUNT_ID_CACHE, 0)
        }
        set(id) {
            settings[KEY_LAST_ACCOUNT_ID_CACHE] = id
        }

    suspend fun getAccount(id: Long): Account? = memCache[id]

    suspend fun saveAccount(account: Account) {
        memCache[account.id] = account
        diskCache = memCache
    }

    suspend fun saveAccounts(accounts: List<Account>) {
        accounts.forEach {
            memCache[it.id] = it
        }

        diskCache = memCache
    }

    suspend fun deleteAccount(id: Long) {
        memCache.remove(id)
        diskCache = memCache
    }

    suspend fun getAllAccounts(): List<Account> = memCache.values.toList()

    companion object {
        private const val CURRENT_ACCOUNT = "CURRENT_ACCOUNT"
        private const val CURRENT_NETWORK = "CURRENT_NETWORK"

        private const val MNEMONIC_COUNTER = "MNEMONIC_COUNTER"

        private const val KEY_ACCOUNT_CACHE = "KEY_ACCOUNT_CACHE"
        private const val KEY_ACCOUNT_ID_CACHE = "KEY_ACCOUNT_ID_CACHE"
        private const val KEY_LAST_ACCOUNT_ID_CACHE = "KEY_LAST_ACCOUNT_ID_CACHE"
    }
}