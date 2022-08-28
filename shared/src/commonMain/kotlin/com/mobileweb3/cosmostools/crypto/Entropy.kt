package com.mobileweb3.cosmostools.crypto

expect object Entropy {

    fun getEntropy(): ByteArray
    
    fun getHDSeed(entropy: ByteArray): ByteArray?
}