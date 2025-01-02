package com.example.tokitoki.domain.usecase

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.CheckEmailRegistered
import com.example.tokitoki.domain.repository.AuthRepository
import javax.inject.Inject

interface CheckEmailRegisteredUseCase {
    suspend operator fun invoke(email: String): ResultWrapper<CheckEmailRegistered>
}

class CheckEmailRegisteredUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : CheckEmailRegisteredUseCase {

    override suspend operator fun invoke(email: String): ResultWrapper<CheckEmailRegistered> {
        return authRepository.checkEmailRegistered(email)
    }
}