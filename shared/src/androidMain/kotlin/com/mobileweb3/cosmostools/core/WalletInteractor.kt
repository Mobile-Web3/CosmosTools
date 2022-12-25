package com.mobileweb3.cosmostools.core

import android.content.Context
import com.mobileweb3.cosmostools.network.api
import com.mobileweb3.cosmostools.repository.BalancesRepository
import com.mobileweb3.cosmostools.repository.NetworksRepository
import com.mobileweb3.cosmostools.wallet.WalletInteractor
import com.mobileweb3.cosmostools.wallet.WalletStorage
import com.russhwolf.settings.AndroidSettings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.serialization.json.Json

fun WalletInteractor.Companion.create(
    ctx: Context,
    balancesRepository: BalancesRepository,
    networksRepository: NetworksRepository,
    withLog: Boolean
) = WalletInteractor(
    WalletStorage(
        AndroidSettings(ctx.getSharedPreferences("wallet_preferences", Context.MODE_PRIVATE)),
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
        }
    ),
    api = api,
    balancesRepository = balancesRepository,
    networksRepository = networksRepository
).also {
    if (withLog) Napier.base(DebugAntilog())
}