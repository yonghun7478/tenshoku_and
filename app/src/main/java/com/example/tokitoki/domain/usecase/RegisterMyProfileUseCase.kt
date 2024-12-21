package com.example.tokitoki.domain.usecase

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.repository.AuthRepository
import javax.inject.Inject

interface RegisterMyProfileUseCase {
    suspend operator fun invoke(
        myProfile: MyProfile,
        thumbnailPath: String
    ): ResultWrapper<MyProfile>
}

class RegisterMyProfileUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : RegisterMyProfileUseCase {
    override suspend fun invoke(
        myProfile: MyProfile,
        thumbnailPath: String
    ): ResultWrapper<MyProfile> {
        return authRepository.registerMyProfile(myProfile, thumbnailPath)
    }
}
