package com.example.tokitoki.domain.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.model.Tokens

interface AuthRepository {
    suspend fun sendVerificationCode(email: String, code: String): ResultWrapper<Tokens>
    suspend fun registerMyProfile(myProfile: MyProfile): ResultWrapper<MyProfile>
    fun saveTokens(token: String, refreshToken: String)
    fun getTokens(): Tokens
}
