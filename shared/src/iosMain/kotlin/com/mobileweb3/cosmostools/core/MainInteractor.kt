package com.mobileweb3.cosmostools.core

import com.mobileweb3.cosmostools.core.datasource.network.SomethingLoader
import com.mobileweb3.cosmostools.core.datasource.storage.SomethingStorage
import com.mobileweb3.cosmostools.interactor.MainInteractor
import com.russhwolf.settings.AppleSettings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.serialization.json.Json
import platform.Foundation.NSUserDefaults

fun MainInteractor.Companion.create(withLog: Boolean) = MainInteractor(
    SomethingLoader(
        IosHttpClient(withLog)
    ),
    SomethingStorage(
        AppleSettings(NSUserDefaults.standardUserDefaults()),
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
        }
    )
).also {
    if (withLog) Napier.base(DebugAntilog())
}