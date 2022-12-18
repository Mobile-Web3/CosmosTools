package com.mobileweb3.cosmostools.network

import com.mobileweb3.cosmostools.core.AndroidHttpClient

actual object ApiBuilder {

    actual fun create(): Api {
        return Api(AndroidHttpClient(withLog = true))
    }
}