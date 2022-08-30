package com.mobileweb3.cosmostools.crypto

expect object EncryptHelper {

    fun encrypt(
        alias: String,
        resource: String,
        withAuth: Boolean
    ): EncryptResult

    fun EncryptResult.getEncData(): String

    fun EncryptResult.getIvData(): String
}

class EncryptResult(var encData: ByteArray, var ivData: ByteArray)