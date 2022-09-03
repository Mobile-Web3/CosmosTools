package com.mobileweb3.cosmostools.crypto

import org.bitcoinj.core.ECKey
import java.math.BigInteger
import java.util.regex.Pattern

actual object PrivateKey {

    actual fun isValid(privateKey: String): Boolean {
        return Pattern
            .compile("^(0x|0X)?[a-fA-F0-9]{64}")
            .matcher(privateKey)
            .matches()
    }

    actual fun generatePubHexFromPrivate(privateKey: String): String {
        val k = ECKey.fromPrivate(BigInteger(privateKey, 16))
        return k.publicKeyAsHex
    }
}