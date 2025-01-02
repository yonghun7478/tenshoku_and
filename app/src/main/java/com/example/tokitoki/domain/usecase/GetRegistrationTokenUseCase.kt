package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.AuthRepository
import javax.inject.Inject

interface GetRegistrationTokenUseCase {
    suspend operator fun invoke(): String
}

class GetRegistrationTokenUseCaseImpl @Inject constructor(private val authRepository: AuthRepository) :
    GetRegistrationTokenUseCase {
    override suspend fun invoke(): String {
        return authRepository.getRegistrationToken()
    }
}