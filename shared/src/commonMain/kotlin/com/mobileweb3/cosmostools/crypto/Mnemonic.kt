package com.mobileweb3.cosmostools.crypto

import com.mobileweb3.cosmostools.core.entity.Account
import com.mobileweb3.cosmostools.wallet.AddressSource

expect object Mnemonic {

    fun getRandomMnemonic(entropy: ByteArray): List<String>

    fun toEntropy(mnemonic: List<String>?): ByteArray?

    fun isValidMnemonic(words: List<String>): Boolean

    fun isValidStringHdSeedFromWords(words: List<String>): Boolean
}

fun getAddressSource(account: Account): AddressSource {
    return if (account.fromMnemonic == true) {
        val entropy = EncryptHelper.decrypt(
            alias = "MNEMONIC_KEY" + account.uuid,
            resource = account.resource!!,
            spec = account.spec!!
        )

        AddressSource.Mnemonic(Mnemonic.getRandomMnemonic(HexUtils.toBytes(entropy)))
    } else {
        var privateKey = EncryptHelper.decrypt(
            alias = "PRIVATE_KEY" + account.uuid,
            resource = account.resource!!,
            spec = account.spec!!
        )

        if (!privateKey.startsWith("0x") && !privateKey.startsWith("0X")) {
            privateKey = "0x$privateKey"
        }

        AddressSource.PrivateKey(privateKey)
    }
}