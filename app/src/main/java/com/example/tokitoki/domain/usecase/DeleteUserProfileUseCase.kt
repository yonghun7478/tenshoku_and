package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.MyProfileRepository
import javax.inject.Inject

interface DeleteUserProfileUseCase {
    suspend operator fun invoke()
}

class DeleteUserProfileUseCaseImpl @Inject constructor(
    private val myProfileRepository: MyProfileRepository
) : DeleteUserProfileUseCase {
    override suspend fun invoke() {
        myProfileRepository.deleteUserProfile()
    }
} 