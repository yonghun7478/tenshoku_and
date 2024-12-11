package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.repository.MyProfileRepository
import javax.inject.Inject

interface UpdateAgeUseCase {
    suspend operator fun invoke(age: String): MyProfile
}

class UpdateAgeUseCaseImpl @Inject constructor(
    private val myProfileRepository: MyProfileRepository
) : UpdateAgeUseCase {
    override suspend operator fun invoke(age: String): MyProfile {
        myProfileRepository.updateUserAge(age)
        return myProfileRepository.getUserProfile() ?: MyProfile()
    }
}