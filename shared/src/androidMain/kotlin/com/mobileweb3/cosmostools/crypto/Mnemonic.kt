package com.mobileweb3.cosmostools.crypto

import org.bitcoinj.crypto.MnemonicCode

actual object Mnemonic {

    actual fun isValidMnemonic(words: List<String>): Boolean {
        val mnemonics = MnemonicCode.INSTANCE.wordList
        for (insert in words) {
            if (!mnemonics.contains(insert)) {
                return false
            }
        }
        return true
    }
}