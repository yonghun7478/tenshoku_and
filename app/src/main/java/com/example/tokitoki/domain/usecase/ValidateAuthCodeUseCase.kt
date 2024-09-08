package com.example.tokitoki.domain.usecase

import javax.inject.Inject

class ValidateAuthCodeUseCase @Inject constructor() {
    suspend operator fun invoke(code: String): Boolean {
        return true
    }
}