package com.mobileweb3.cosmostools.network

import com.mobileweb3.cosmostools.core.IosHttpClient

actual object ApiBuilder {

    actual fun create(): Api {
        return Api(IosHttpClient(withLog = true))
    }
}