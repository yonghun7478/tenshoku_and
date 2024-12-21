package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.model.Tokens

interface AuthRepository {
    suspend fun sendVerificationCode(email: String, code: String): Tokens
    suspend fun registerMyProfile(myProfile: MyProfile): MyProfile
    fun saveTokens(token: String, refreshToken: String)
    fun getTokens(): Tokens
}
