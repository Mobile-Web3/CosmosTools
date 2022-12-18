package com.mobileweb3.cosmostools.core

import com.mobileweb3.cosmostools.network.BASE_URL
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*

internal fun IosHttpClient(withLog: Boolean) = HttpClient(Darwin) {
    engine {
        configureRequest {
            setAllowsCellularAccess(true)
        }
    }
    defaultRequest {
        url(BASE_URL)
    }
    install(ContentNegotiation) {
        json()
    }
    if (withLog) install(Logging) {
        level = LogLevel.HEADERS
        logger = object : Logger {
            override fun log(message: String) {
                Napier.v(tag = "IosHttpClient", message = message)
            }
        }
    }
}