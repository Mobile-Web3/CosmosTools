package com.mobileweb3.cosmostools.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.KeyStore.PrivateKeyEntry
import java.security.KeyStoreException
import java.security.Signature

actual object EncryptHelper {

    private const val KEYSTORE = "AndroidKeyStore"
    private const val TYPE_RSA = "RSA"
    private const val SIGNATURE_SHA256withRSA = "SHA256withRSA"

    private fun loadKeyStore(): KeyStore {
        return KeyStore.getInstance(KEYSTORE).apply {
            load(null)
        }
    }

    private fun generateKeyPairIfNecessary(keyStore: KeyStore, alias: String): Boolean {
        try {
            return keyStore.containsAlias(alias) || generateKeyPair(alias)
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        }
        return false
    }

    private fun generateKeyPair(alias: String): Boolean {
        return try {
            val kpGenerator: KeyPairGenerator = KeyPairGenerator.getInstance(TYPE_RSA, KEYSTORE)
            kpGenerator.initialize(
                KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_SIGN)
                    .setDigests(KeyProperties.DIGEST_SHA256)
                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                    .build()
            )
            kpGenerator.generateKeyPair()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    actual fun encryptPin(pin: String): String {
        val alias = "PASSWORD_KEY"
        val data = pin.toByteArray()
        val keyStore = loadKeyStore()
        generateKeyPairIfNecessary(keyStore, alias)
        val entry = keyStore.getEntry(alias, null) as PrivateKeyEntry
        val s = Signature.getInstance(SIGNATURE_SHA256withRSA)
        s.initSign(entry.privateKey)
        s.update(data)
        val signature = s.sign()
        return Base64.encodeToString(signature, Base64.DEFAULT)
    }

    actual fun verifyPin(pin: String, signatureStr: String): Boolean {
        val alias = "PASSWORD_KEY"
        val data = pin.toByteArray()
        val keyStore = loadKeyStore()

        val signature: ByteArray = Base64.decode(signatureStr, Base64.DEFAULT)
        val s = Signature.getInstance(SIGNATURE_SHA256withRSA)

        s.initVerify(keyStore.getCertificate(alias))
        s.update(data)
        return s.verify(signature)
    }
}