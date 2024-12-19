package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.repository.AuthRepository
import javax.inject.Inject

interface RegisterMyProfileUseCase {
    suspend fun execute(myProfile: MyProfile): MyProfile
}

class RegisterMyProfileUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : RegisterMyProfileUseCase {
    override suspend fun execute(myProfile: MyProfile): MyProfile {
        return authRepository.registerMyProfile(myProfile)
    }
}
