package com.mobileweb3.cosmostools.crypto

import com.mobileweb3.cosmostools.BuildConfig
import org.bitcoinj.crypto.MnemonicCode
import org.bitcoinj.crypto.MnemonicException

actual object Mnemonic {

    actual fun getRandomMnemonic(entropy: ByteArray): List<String> {
        return try {
            MnemonicCode.INSTANCE.toMnemonic(entropy)
        } catch (e: MnemonicException.MnemonicLengthException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            emptyList()
        }
    }

    actual fun toEntropy(mnemonic: List<String>?): ByteArray? {
        return try {
            MnemonicCode().toEntropy(mnemonic)
        } catch (e: Exception) {
            null
        }
    }

    actual fun isValidMnemonic(words: List<String>): Boolean {
        val mnemonics = MnemonicCode.INSTANCE.wordList
        for (insert in words) {
            if (!mnemonics.contains(insert)) {
                return false
            }
        }
        return true
    }

    actual fun isValidStringHdSeedFromWords(words: List<String>): Boolean {
        return toEntropy(words)?.let { Entropy.getHDSeed(it) } != null
    }
}