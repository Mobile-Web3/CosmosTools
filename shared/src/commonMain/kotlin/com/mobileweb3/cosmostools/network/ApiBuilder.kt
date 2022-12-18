package com.mobileweb3.cosmostools.network

var api: Api? = null
    private set
    get() = field ?: ApiBuilder.create()

expect object ApiBuilder {

    fun create(): Api
}