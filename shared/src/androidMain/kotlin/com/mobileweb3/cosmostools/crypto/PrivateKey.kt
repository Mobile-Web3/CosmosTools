package com.mobileweb3.cosmostools.crypto

import java.util.regex.Pattern

actual object PrivateKey {

    actual fun isValid(privateKey: String): Boolean {
        return Pattern
            .compile("^(0x|0X)?[a-fA-F0-9]{64}")
            .matcher(privateKey)
            .matches()
    }
}