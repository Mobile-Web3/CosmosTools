package com.mobileweb3.cosmostools.crypto

import kotlin.jvm.JvmOverloads

/**
 * Utilities for going to and from ASCII-HEX representation.
 */
object HexUtils {

    /**
     * Encodes an array of bytes as hex symbols.
     *
     * @param bytes
     * the array of bytes to encode
     * @param separator
     * the separator to use between two bytes, can be null
     * @return the resulting hex string
     */
    /**
     * Encodes an array of bytes as hex symbols.
     *
     * @param bytes
     * the array of bytes to encode
     * @return the resulting hex string
     */
    @JvmOverloads
    fun toHex(bytes: ByteArray, separator: String? = null): String {
        return toHex(bytes, 0, bytes.size, separator)
    }

    /**
     * Encodes a single byte to hex symbols.
     *
     * @param b the byte to encode
     * @return the resulting hex string
     */
    fun toHex(b: Byte): String {
        val sb = StringBuilder()
        appendByteAsHex(sb, b)
        return sb.toString()
    }
    /**
     * Encodes an array of bytes as hex symbols.
     *
     * @param bytes
     * the array of bytes to encode
     * @param offset
     * the start offset in the array of bytes
     * @param length
     * the number of bytes to encode
     * @param separator
     * the separator to use between two bytes, can be null
     * @return the resulting hex string
     */
    /**
     * Encodes an array of bytes as hex symbols.
     *
     * @param bytes
     * the array of bytes to encode
     * @param offset
     * the start offset in the array of bytes
     * @param length
     * the number of bytes to encode
     * @return the resulting hex string
     */
    @JvmOverloads
    fun toHex(bytes: ByteArray, offset: Int, length: Int, separator: String? = null): String {
        val result = StringBuilder()
        for (i in 0 until length) {
            val unsignedByte: Int = bytes[i + offset].toInt() and 0xff
            if (unsignedByte < 16) {
                result.append("0")
            }
            result.append(unsignedByte.toString(16))
            if (separator != null && i + 1 < length) {
                result.append(separator)
            }
        }
        return result.toString()
    }

    /**
     * Get the byte representation of an ASCII-HEX string.
     *
     * @param hexString
     * The string to convert to bytes
     * @return The byte representation of the ASCII-HEX string.
     */
    fun toBytes(hexString: String?): ByteArray {
        if (hexString == null || hexString.length % 2 != 0) {
            throw RuntimeException("Input string must contain an even number of characters")
        }
        val hex = hexString.toCharArray()
        val length = hex.size / 2
        val raw = ByteArray(length)
        for (i in 0 until length) {
            val high = hex[i * 2].digitToIntOrNull(16) ?: -1
            val low = hex[i * 2 + 1].digitToIntOrNull(16) ?: -1
            if (high < 0 || low < 0) {
                throw RuntimeException("Invalid hex digit " + hex[i * 2] + hex[i * 2 + 1])
            }
            var value = high shl 4 or low
            if (value > 127) value -= 256
            raw[i] = value.toByte()
        }
        return raw
    }

    fun toBytesReversed(hexString: String?): ByteArray {
        val rawBytes = toBytes(hexString)
        for (i in 0 until rawBytes.size / 2) {
            val temp = rawBytes[rawBytes.size - i - 1]
            rawBytes[rawBytes.size - i - 1] = rawBytes[i]
            rawBytes[i] = temp
        }
        return rawBytes
    }

    fun appendByteAsHex(sb: StringBuilder, b: Byte) {
        val unsignedByte: Int = b.toInt() and 0xFF
        if (unsignedByte < 16) {
            sb.append("0")
        }
        sb.append(unsignedByte.toString(16))
    }
}