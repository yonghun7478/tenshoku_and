package com.example.tokitoki.domain.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.model.Tokens
import com.example.tokitoki.domain.model.VerifyGoogleToken

interface AuthRepository {
    suspend fun sendVerificationCode(email: String, code: String): ResultWrapper<Tokens>
    suspend fun registerMyProfile(
        myProfile: MyProfile,
        thumbnailPath: String
    ): ResultWrapper<MyProfile>

    suspend fun sendGoogleToken(id: String, idToken: String): ResultWrapper<VerifyGoogleToken>

    fun saveTokens(token: String, refreshToken: String)
    fun getTokens(): Tokens
}
