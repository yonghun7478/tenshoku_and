package com.example.tenshoku_and.data.local

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptionHelper @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val KEY_ALIAS = "db_encryption_key"
        private const val PREFS_NAME = "my_encrypted_prefs"
        private const val PASSWORD_KEY = "db_password"
    }

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

    // MasterKey 생성
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // EncryptedSharedPreferences 생성
    private val sharedPrefs = EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getEncryptedPassword(): String {
        return sharedPrefs.getString(PASSWORD_KEY, null) ?: run {
            val key = getKey()
            val password = generateRandomPassword()
            val encryptedBytes = encrypt(password, key)
            sharedPrefs.edit().putString(PASSWORD_KEY, encryptedBytes.toString(StandardCharsets.UTF_8)).apply()
            encryptedBytes.toString(StandardCharsets.UTF_8)
        }
    }

    private fun getKey(): SecretKey {
        return if (keyStore.containsAlias(KEY_ALIAS)) {
            keyStore.getKey(KEY_ALIAS, null) as SecretKey
        } else {
            generateKey()
        }
    }

    private fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }

    private fun encrypt(text: String, key: SecretKey): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return cipher.doFinal(text.toByteArray())
    }

    private fun generateRandomPassword(): String {
        // 랜덤 비밀번호 생성 로직 (예: UUID 사용)
        return UUID.randomUUID().toString()
    }
}