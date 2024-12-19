package com.example.tokitoki.data.local

import android.content.Context

interface TokenPreferences {
    fun saveAccessToken(token: String) // Access Token 저장
    fun getAccessToken(): String? // Access Token 읽기
    fun saveRefreshToken(token: String) // Refresh Token 저장
    fun getRefreshToken(): String? // Refresh Token 읽기
    fun clearTokens() // 모든 토큰 삭제
}

class TokenPreferencesImpl(
    private val context: Context
) : TokenPreferences {
    private val sharedPreferences = context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE)

    override fun saveAccessToken(token: String) {
        sharedPreferences.edit().putString("access_token", token).apply()
    }

    override fun getAccessToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    override fun saveRefreshToken(token: String) {
        sharedPreferences.edit().putString("refresh_token", token).apply()
    }

    override fun getRefreshToken(): String? {
        return sharedPreferences.getString("refresh_token", null)
    }

    override fun clearTokens() {
        sharedPreferences.edit().remove("access_token").remove("refresh_token").apply()
    }
}
