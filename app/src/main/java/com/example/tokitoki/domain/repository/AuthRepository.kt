package com.example.tokitoki.domain.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.CheckEmailRegistered
import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.model.Tokens
import com.example.tokitoki.domain.model.VerifyEmail
import com.example.tokitoki.domain.model.VerifyGoogleToken

interface AuthRepository {
    suspend fun verifyEmail(email: String, code: String): ResultWrapper<VerifyEmail>
    suspend fun registerMyProfile(
        myProfile: MyProfile,
        thumbnailPath: String
    ): ResultWrapper<MyProfile>

    suspend fun verifyGoogleToken(id: String, idToken: String): ResultWrapper<VerifyGoogleToken>

    fun saveTokens(token: String, refreshToken: String)
    fun getTokens(): Tokens

    fun saveRegistrationToken(registrationToken: String)
    fun getRegistrationToken(): String

    suspend fun checkEmailRegistered(email: String): ResultWrapper<CheckEmailRegistered>
}
