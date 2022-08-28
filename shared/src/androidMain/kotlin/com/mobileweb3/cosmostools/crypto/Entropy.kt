package com.mobileweb3.cosmostools.crypto

import org.bitcoinj.crypto.MnemonicCode
import java.security.SecureRandom

actual object Entropy {

    actual fun getEntropy(): ByteArray {
        val seed = ByteArray(32)
        SecureRandom().nextBytes(seed)
        return seed
    }

    actual fun getHDSeed(entropy: ByteArray): ByteArray? {
        return try {
            MnemonicCode.toSeed(MnemonicCode.INSTANCE.toMnemonic(entropy), "")
        } catch (e: Exception) {
            null
        }
    }
}