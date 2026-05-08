package com.example.data.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val FILE_NAME   = "secure_prefs"
        private const val KEY_ACCESS  = "auth_token"
        private const val KEY_REFRESH = "refresh_token"
        private const val KEY_USER_ID = "user_id"
    }

    private val sharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // ── Access Token ──────────────────────────────────────────
    fun saveAuthToken(token: String) {
        sharedPreferences.edit().putString(KEY_ACCESS, token).apply()
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS, null)
    }

    fun clearAuthToken() {
        sharedPreferences.edit().remove(KEY_ACCESS).apply()
    }

    // ── Refresh Token ─────────────────────────────────────────
    fun saveRefreshToken(token: String) {
        sharedPreferences.edit().putString(KEY_REFRESH, token).apply()
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(KEY_REFRESH, null)
    }

    // ── Save Both at Once ─────────────────────────────────────
    fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit()
            .putString(KEY_ACCESS, accessToken)
            .putString(KEY_REFRESH, refreshToken)
            .apply()
    }

    // ── Clear Everything ──────────────────────────────────────
    fun clearTokens() {
        sharedPreferences.edit()
            .remove(KEY_ACCESS)
            .remove(KEY_REFRESH)
            .remove(KEY_USER_ID)
            .apply()
    }

    // ── User ID ──────────────────────────────────────────────
    fun saveUserId(userId: String) {
        sharedPreferences.edit().putString(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    fun clearUserId() {
        sharedPreferences.edit().remove(KEY_USER_ID).apply()
    }

    fun isLoggedIn(): Boolean = getAuthToken() != null
}