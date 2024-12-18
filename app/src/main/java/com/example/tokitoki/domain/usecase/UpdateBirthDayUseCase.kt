package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.repository.MyProfileRepository
import javax.inject.Inject

interface UpdateBirthDayUseCase {
    suspend operator fun invoke(birthDay: String): MyProfile
}

class UpdateBirthDayUseCaseImpl @Inject constructor(
    private val myProfileRepository: MyProfileRepository
) : UpdateBirthDayUseCase {
    override suspend operator fun invoke(birthDay: String): MyProfile {
        myProfileRepository.updateUserBirthday(birthDay)
        return myProfileRepository.getUserProfile() ?: MyProfile()
    }
}