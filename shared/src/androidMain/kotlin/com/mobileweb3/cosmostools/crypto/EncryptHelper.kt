package com.mobileweb3.cosmostools.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.KeyStore.PrivateKeyEntry
import java.security.KeyStoreException
import java.security.Signature
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

actual object EncryptHelper {

    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val KEYSTORE = "AndroidKeyStore"
    private const val TYPE_RSA = "RSA"
    private const val SIGNATURE_SHA256withRSA = "SHA256withRSA"

    actual fun encrypt(
        alias: String,
        resource: String,
        withAuth: Boolean
    ): EncryptResult {
        val cipher: Cipher = getEncodeCipher(alias, withAuth)
        val end = cipher.doFinal(resource.toByteArray(charset("UTF-8")))
        return EncryptResult(end, cipher.iv)
    }

    actual fun EncryptResult.getEncData(): String {
        return Base64.encodeToString(encData, 0)
    }

    actual fun EncryptResult.getIvData(): String {
        return Base64.encodeToString(ivData, 0)
    }

    private fun getEncodeCipher(alias: String, withAuth: Boolean): Cipher {
        val encCipher = Cipher.getInstance(TRANSFORMATION)
        val keyStore: KeyStore = loadKeyStore()
        generateKeyIfNecessary(keyStore, alias, withAuth)
        val key = keyStore.getKey(alias, null) as SecretKey
        encCipher.init(Cipher.ENCRYPT_MODE, key)
        return encCipher
    }

    private fun loadKeyStore(): KeyStore {
        return KeyStore.getInstance(KEYSTORE).apply {
            load(null)
        }
    }

    private fun generateKeyIfNecessary(keyStore: KeyStore, alias: String, withAuth: Boolean): Boolean {
        try {
            return keyStore.containsAlias(alias) || generateKey(alias, withAuth)
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        }
        return false
    }

    private fun generateKeyPairIfNecessary(keyStore: KeyStore, alias: String): Boolean {
        try {
            return keyStore.containsAlias(alias) || generateKeyPair(alias)
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        }
        return false
    }

    private fun generateKey(alias: String, withAuth: Boolean): Boolean {
        return try {
            val keyGenerator: KeyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                KEYSTORE
            )
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    alias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setUserAuthenticationRequired(withAuth)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            )
            keyGenerator.generateKey()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
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
}