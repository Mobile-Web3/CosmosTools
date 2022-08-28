package com.mobileweb3.cosmostools.crypto

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Arrays

/**
 * represents the result of a SHA256 hashing operation prefer to use the static
 * factory methods.
 */
class Sha256(val bytes: ByteArray) {

    override fun equals(other: Any?): Boolean {
        if (other === this) {
            return true
        }
        return if (!(other is Sha256)) false else Arrays.equals(bytes, other.bytes)
    }

    override fun toString(): String {
        return HexUtils.toHex(bytes)
    }

    fun equalsFromOffset(toCompareData: ByteArray, offsetInCompareData: Int, len: Int): Boolean {
        if (offsetInCompareData < 0 || len < 0 || bytes.size <= len || toCompareData.size <= offsetInCompareData
        ) {
            return false
        }
        for (i in 0 until len) {
            if (bytes[i] != toCompareData[offsetInCompareData + i]) {
                return false
            }
        }
        return true
    }

    fun length(): Int {
        return HASH_LENGTH
    }

    companion object {

        const val HASH_LENGTH = 32
        val ZERO_HASH = Sha256(ByteArray(HASH_LENGTH))

        //cannot happen
        val sha256Digest: MessageDigest
            get() = try {
                MessageDigest.getInstance("SHA-256")
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException(e) //cannot happen
            }

        fun from(data: ByteArray): Sha256 {
            val digest: MessageDigest = sha256Digest
            digest.update(data, 0, data.size)
            return Sha256(digest.digest())
        }

        fun from(data: ByteArray, offset: Int, length: Int): Sha256 {
            val digest: MessageDigest = sha256Digest
            digest.update(data, offset, length)
            return Sha256(digest.digest())
        }

        fun from(data1: ByteArray, data2: ByteArray): Sha256 {
            val digest: MessageDigest = sha256Digest
            digest.update(data1, 0, data1.size)
            digest.update(data2, 0, data2.size)
            return Sha256(digest.digest())
        }

        fun doubleHash(data: ByteArray, offset: Int, length: Int): Sha256 {
            val digest: MessageDigest = sha256Digest
            digest.update(data, offset, length)
            return Sha256(digest.digest(digest.digest()))
        }
    }
}