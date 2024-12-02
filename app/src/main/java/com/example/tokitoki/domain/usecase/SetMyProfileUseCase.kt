package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.repository.MyProfileRepository
import javax.inject.Inject

interface SetMyProfileUseCase {
    suspend operator fun invoke(myProfile: MyProfile): MyProfile
}

class SetMyProfileUseCaseImpl @Inject constructor(
    private val myProfileRepository: MyProfileRepository
) : SetMyProfileUseCase {
    override suspend operator fun invoke(myProfile: MyProfile): MyProfile {
        myProfileRepository.saveUserProfile(myProfile)
        return myProfileRepository.getUserProfile() ?: MyProfile()
    }
}