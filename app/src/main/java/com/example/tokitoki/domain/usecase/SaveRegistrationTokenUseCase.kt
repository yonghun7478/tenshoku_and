package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.AuthRepository
import javax.inject.Inject

interface SaveRegistrationTokenUseCase {
    suspend operator fun invoke(token: String)
}

class SaveRegistrationTokenUseCaseImpl @Inject constructor(private val authRepository: AuthRepository) :
    SaveRegistrationTokenUseCase {
    override suspend fun invoke(token: String) {
        authRepository.saveRegistrationToken(token)
    }
}
