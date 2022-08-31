package com.mobileweb3.cosmostools.crypto

expect object Mnemonic {

    fun getRandomMnemonic(entropy: ByteArray): List<String>

    fun toEntropy(mnemonic: List<String>?): ByteArray?

    fun isValidMnemonic(words: List<String>): Boolean

    fun isValidStringHdSeedFromWords(words: List<String>): Boolean
}