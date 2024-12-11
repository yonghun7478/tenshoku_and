package com.example.tokitoki.data.local

import android.content.Context

interface DbVersionPreferences {
    fun saveDbVersion(version: String) // DB 버전 저장
    fun getDbVersion(): String? // DB 버전 읽기
}

class DbVersionPreferencesImpl(
    private val context: Context
) : DbVersionPreferences {
    private val sharedPreferences = context.getSharedPreferences("db_version_prefs", Context.MODE_PRIVATE)

    override fun saveDbVersion(version: String) {
        sharedPreferences.edit().putString("db_version", version).apply()
    }

    override fun getDbVersion(): String? {
        return sharedPreferences.getString("db_version", null)
    }
}
