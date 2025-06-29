package com.example.tokitoki.domain.usecase

import javax.inject.Inject

interface ValidateEmailFormatUseCase {
    suspend operator fun invoke(email: String): Boolean
}

class ValidateEmailFormatUseCaseImpl @Inject constructor() : ValidateEmailFormatUseCase {
    override suspend operator fun invoke(email: String): Boolean {
        // 이메일 형식을 검증하는 정규 표현식
        val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")

        // 입력된 이메일 문자열이 정규 표현식과 일치하는지 확인
        return emailPattern.matches(email)
    }
}
