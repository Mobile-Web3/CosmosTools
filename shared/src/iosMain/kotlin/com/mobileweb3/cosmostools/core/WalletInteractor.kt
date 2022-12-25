package com.mobileweb3.cosmostools.core

import com.mobileweb3.cosmostools.network.api
import com.mobileweb3.cosmostools.wallet.WalletInteractor
import com.mobileweb3.cosmostools.wallet.WalletStorage
import com.russhwolf.settings.AppleSettings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.serialization.json.Json
import platform.Foundation.NSUserDefaults

fun WalletInteractor.Companion.create(withLog: Boolean) = WalletInteractor(
    WalletStorage(
        AppleSettings(NSUserDefaults.standardUserDefaults()),
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
        }
    ),
    api
).also {
    if (withLog) Napier.base(DebugAntilog())
}