package com.mobileweb3.cosmostools.core

import com.mobileweb3.cosmostools.network.BASE_URL
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import java.util.concurrent.TimeUnit
import io.ktor.serialization.kotlinx.json.*

internal fun AndroidHttpClient(withLog: Boolean) = HttpClient(OkHttp) {
    engine {
        config {
            retryOnConnectionFailure(true)
            connectTimeout(5, TimeUnit.SECONDS)
        }
    }
    install(ContentNegotiation) {
        json()
    }
    defaultRequest {
        url(BASE_URL)
    }
    if (withLog) install(Logging) {
        level = LogLevel.HEADERS
        logger = object : Logger {
            override fun log(message: String) {
                Napier.v(tag = "AndroidHttpClient", message = message)
            }
        }
    }
}