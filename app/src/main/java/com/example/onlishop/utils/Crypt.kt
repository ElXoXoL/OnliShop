package com.example.onlishop.utils

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class CryptImpl(private val key: String, private val iv: String): Crypt {

    companion object {
        private const val ALGORITHM = "Blowfish"
        private const val MODE = "Blowfish/CBC/PKCS5Padding"
    }

    override fun encrypt(value: String): String {
        return try {
            val secretKeySpec = SecretKeySpec(key.toByteArray(), ALGORITHM)
            val cipher: Cipher = Cipher.getInstance(MODE)
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, IvParameterSpec(iv.toByteArray()))
            val values: ByteArray = cipher.doFinal(value.toByteArray())
            Base64.encodeToString(values, Base64.DEFAULT) ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    override fun decrypt(value: String): String {
        return try {
            val values: ByteArray = Base64.decode(value, Base64.DEFAULT)
            val secretKeySpec = SecretKeySpec(key.toByteArray(), ALGORITHM)
            val cipher: Cipher = Cipher.getInstance(MODE)
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(iv.toByteArray()))
            String(cipher.doFinal(values))
        } catch (e: Exception) {
            ""
        }
    }

}

interface Crypt {
    fun encrypt(value: String): String
    fun decrypt(value: String): String
}