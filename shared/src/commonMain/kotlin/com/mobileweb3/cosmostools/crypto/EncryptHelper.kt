package com.mobileweb3.cosmostools.crypto

expect object EncryptHelper {

    fun encryptPin(pin: String): String

    fun verifyPin(pin: String, signatureStr: String): Boolean
}