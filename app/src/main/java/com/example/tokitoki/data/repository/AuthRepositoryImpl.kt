package com.example.tokitoki.data.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.data.local.TokenPreferences
import com.example.tokitoki.data.model.CheckEmailRegisteredResponse
import com.example.tokitoki.data.model.VerifyEmailResponse
import com.example.tokitoki.data.model.VerifyGoogleTokenResponse
import com.example.tokitoki.domain.model.CheckEmailRegistered
import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.model.Tokens
import com.example.tokitoki.domain.model.VerifyEmail
import com.example.tokitoki.domain.model.VerifyGoogleToken
import com.example.tokitoki.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val tokenPreferences: TokenPreferences
) : AuthRepository {
    override suspend fun verifyEmail(email: String, code: String): ResultWrapper<VerifyEmail> {
        val res = VerifyEmailResponse("dummyToken")
        return ResultWrapper.Success(VerifyEmail(res.registrationToken))
    }

    override suspend fun registerMyProfile(
        myProfile: MyProfile,
        thumbnailPath: String
    ): ResultWrapper<MyProfile> {
        return ResultWrapper.Success(
            MyProfile(
                myProfile.id,
                myProfile.name,
                myProfile.birthDay,
                myProfile.isMale,
                myProfile.mySelfSentenceId,
                "asdf@asdf.com",
                "https://img.vogue.co.kr/vogue/2025/03/style_67e0bd4865315-1120x1400.jpg"
            )
        )
    }

    override suspend fun verifyGoogleToken(
        id: String,
        idToken: String
    ): ResultWrapper<VerifyGoogleToken> {

        val result = VerifyGoogleTokenResponse(
            registrationToken = "1234"
        )

        return ResultWrapper.Success(
            VerifyGoogleToken(
                registrationToken = result.registrationToken
            )
        )
    }

    override fun saveTokens(token: String, refreshToken: String) {
        tokenPreferences.saveAccessToken(token)
        tokenPreferences.saveRefreshToken(refreshToken)
    }

    override fun getTokens(): Tokens {
        val accessToken = tokenPreferences.getAccessToken() ?: ""
        val refreshToken = tokenPreferences.getRefreshToken() ?: ""
        return Tokens(accessToken, refreshToken)
    }

    override fun clearTokens() {
        tokenPreferences.clearTokens()
    }


    override fun saveRegistrationToken(registrationToken: String) {
        tokenPreferences.saveRegistrationToken(registrationToken)
    }

    override fun getRegistrationToken(): String {
        return tokenPreferences.getRegistrationToken()
    }

    override suspend fun checkEmailRegistered(email: String): ResultWrapper<CheckEmailRegistered> {

        val rowResponse = if (email.contains("true")) {
            CheckEmailRegisteredResponse(
                isRegistered = true,
                accessToken = "asdfasdf",
                refreshToken = "asdfadf"
            )
        } else {
            CheckEmailRegisteredResponse(
                isRegistered = false,
                accessToken = "",
                refreshToken = ""
            )
        }

        return ResultWrapper.Success(
            CheckEmailRegistered(
                rowResponse.isRegistered,
                rowResponse.accessToken,
                rowResponse.refreshToken
            )
        )
    }
}
