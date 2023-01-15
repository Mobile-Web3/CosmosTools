package com.mobileweb3.cosmostools.crypto

expect object PrivateKey {

    fun isValid(privateKey: String): Boolean
}