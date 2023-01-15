package com.mobileweb3.cosmostools.crypto

expect object Mnemonic {

    fun isValidMnemonic(words: List<String>): Boolean
}