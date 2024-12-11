package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.repository.MyProfileRepository

import javax.inject.Inject

interface GetMyProfileUseCase {
    suspend operator fun invoke(): MyProfile
}

class GetMyProfileUseCaseImpl @Inject constructor(
    private val myProfileRepository: MyProfileRepository
) : GetMyProfileUseCase {
    override suspend operator fun invoke(): MyProfile {
        return myProfileRepository.getUserProfile() ?: MyProfile()
    }
}