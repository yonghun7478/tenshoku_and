package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.repository.MyProfileRepository

import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
    private val myProfileRepository: MyProfileRepository
) {
    suspend operator fun invoke(): MyProfile {
        return myProfileRepository.getUserProfile() ?: MyProfile()
    }
}
