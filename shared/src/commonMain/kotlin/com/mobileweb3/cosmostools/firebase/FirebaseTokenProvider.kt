package com.mobileweb3.cosmostools.firebase

expect object FirebaseTokenProvider {

    suspend fun get(): String?
}