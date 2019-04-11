package com.example.vshcheglov.webshop.presentation.helpres

import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.security.keystore.KeyGenParameterSpec
import android.os.Build
import android.annotation.TargetApi
import android.util.Base64
import androidx.biometric.BiometricPrompt
import java.security.*
import java.security.spec.MGF1ParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource

@TargetApi(Build.VERSION_CODES.M)
class Encryptor(
    private val keyAlias: String,
    private val keyStoreName: String,
    private val cipherTransformation: String
) {
    private lateinit var keyStore: KeyStore
    private lateinit var keyPairGenerator: KeyPairGenerator
    private lateinit var cipher: Cipher

    val cryptoObject: BiometricPrompt.CryptoObject?
        get() {
            return if (initComponents() && initCipherMode(Cipher.DECRYPT_MODE)) {
                BiometricPrompt.CryptoObject(cipher)
            } else null
        }

    fun encode(inputString: String): String? {
        try {
            if (initComponents() && initCipherMode(Cipher.ENCRYPT_MODE)) {
                val bytes = cipher.doFinal(inputString.toByteArray())
                return Base64.encodeToString(bytes, Base64.NO_WRAP)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        return null
    }


    fun decode(encodedString: String, cipher: Cipher): String? {
        try {
            val bytes = Base64.decode(encodedString, Base64.NO_WRAP)
            return String(cipher.doFinal(bytes))
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        return null
    }

    private fun initComponents(): Boolean {
        return initKeyStore() && initCipher() && initKey()
    }


    private fun initKeyStore(): Boolean {
        try {
            keyStore = KeyStore.getInstance(keyStoreName)
            keyStore.load(null)
            return true
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        return false
    }


    @TargetApi(Build.VERSION_CODES.M)
    private fun initKeyPairGenerator(): Boolean {
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, keyStoreName)
            return true
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        return false
    }

    private fun initCipher(): Boolean {
        try {
            cipher = Cipher.getInstance(cipherTransformation)
            return true
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        return false
    }

    private fun initKey(): Boolean {
        try {
            return keyStore.containsAlias(keyAlias) || generateNewKey()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return false
    }


    @TargetApi(Build.VERSION_CODES.M)
    private fun generateNewKey(): Boolean {

        if (initKeyPairGenerator()) {

            try {
                keyPairGenerator.initialize(
                    KeyGenParameterSpec.Builder(
                        keyAlias,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                        .setUserAuthenticationRequired(true)
                        .build()
                )
                keyPairGenerator.generateKeyPair()
                return true
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

        }
        return false
    }


    private fun initCipherMode(mode: Int): Boolean {
        try {
            keyStore.load(null)

            when (mode) {
                Cipher.ENCRYPT_MODE -> initEncodeCipher(mode)

                Cipher.DECRYPT_MODE -> initDecodeCipher(mode)
                else -> return false //this cipher is only for encode\decode
            }
            return true

        } catch (exception: KeyPermanentlyInvalidatedException) {
            exception.printStackTrace()
            deleteInvalidKey()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        return false
    }

    private fun initDecodeCipher(mode: Int) {
        val key = keyStore.getKey(keyAlias, null) as PrivateKey
        cipher.init(mode, key)
    }

    private fun initEncodeCipher(mode: Int) {
        val key = keyStore.getCertificate(keyAlias).publicKey

        // workaround for using public key
        // from https://developer.android.com/reference/android/security/keystore/KeyGenParameterSpec.html
        val unrestricted =
            KeyFactory.getInstance(key.algorithm).generatePublic(X509EncodedKeySpec(key.encoded))
        // from https://code.google.com/p/android/issues/detail?id=197719
        val spec = OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT)

        cipher.init(mode, unrestricted, spec)
    }

    private fun deleteInvalidKey() {
        if (initKeyStore()) {
            try {
                keyStore.deleteEntry(keyAlias)
            } catch (e: KeyStoreException) {
                e.printStackTrace()
            }

        }
    }
}