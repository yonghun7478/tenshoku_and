package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.model.Tokens

interface AuthRepository {
    suspend fun sendVerificationCode(code: String): Tokens
    suspend fun registerMyProfile(myProfile: MyProfile): MyProfile
}
