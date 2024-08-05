package com.example.tenshoku_and.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface UserPreferences {
    fun saveUserNameFrompreferences(name: String)
    fun getUserNameFrompreferences(): String?
}

class UserPreferencesImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserPreferences {
    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    override fun saveUserNameFrompreferences(name: String) {
        sharedPreferences.edit().putString("user_name", name).apply()
    }

    override fun getUserNameFrompreferences(): String? {
        return sharedPreferences.getString("user_name", null)
    }
}
