package com.example.tokitoki.domain.usecase

import javax.inject.Inject

interface ValidateAuthCodeUseCase {
    suspend operator fun invoke(code: String): Boolean
}

class ValidateAuthCodeUseCaseImpl @Inject constructor() : ValidateAuthCodeUseCase {
    override suspend operator fun invoke(code: String): Boolean {
        return code == "123456"
    }
}
