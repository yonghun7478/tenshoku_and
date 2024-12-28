package com.example.tokitoki.domain.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.Credential
import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.model.Tokens

interface AuthRepository {
    suspend fun sendVerificationCode(email: String, code: String): ResultWrapper<Tokens>
    suspend fun registerMyProfile(myProfile: MyProfile, thumbnailPath: String): ResultWrapper<MyProfile>
    suspend fun signWithGoogle(): Credential

    fun saveTokens(token: String, refreshToken: String)
    fun getTokens(): Tokens
}
