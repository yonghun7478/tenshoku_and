package com.example.tokitoki.data.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.data.local.TokenPreferences
import com.example.tokitoki.data.model.TokensResponse
import com.example.tokitoki.domain.converter.TokensConverter
import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.model.Tokens
import com.example.tokitoki.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val tokenPreferences: TokenPreferences
) : AuthRepository {
    override suspend fun sendVerificationCode(email: String, code: String): ResultWrapper<Tokens> {
        val res = TokensResponse("dummyToken", "dummyRefreshToken")
        return ResultWrapper.Success(TokensConverter.fromResponse(res))
    }

    override suspend fun registerMyProfile(myProfile: MyProfile): ResultWrapper<MyProfile> {
        return ResultWrapper.Success(
            MyProfile(
                myProfile.id,
                myProfile.name,
                myProfile.birthDay,
                myProfile.isMale,
                myProfile.mySelfSentenceId
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
}
