package com.mobileweb3.cosmostools.network

val api: Api = ApiBuilder.create()

expect object ApiBuilder {

    fun create(): Api
}