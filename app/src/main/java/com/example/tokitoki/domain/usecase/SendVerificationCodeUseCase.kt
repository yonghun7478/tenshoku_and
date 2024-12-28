package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.Tokens
import com.example.tokitoki.domain.repository.AuthRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import com.example.tokitoki.common.ResultWrapper

interface SendVerificationCodeUseCase {
    suspend operator fun invoke(email: String, code: String): ResultWrapper<Tokens>
}

class SendVerificationCodeUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : SendVerificationCodeUseCase {
    override suspend fun invoke(email: String, code: String): ResultWrapper<Tokens> {
        return try {
            // 성공적으로 토큰을 반환하면 Success로 감쌈
            val result = authRepository.sendVerificationCode(email, code)
            return result
        } catch (e: IOException) {
            // 네트워크 오류 처리
            ResultWrapper.Error(ResultWrapper.ErrorType.ExceptionError("Network error occurred: ${e.localizedMessage}"))
        } catch (e: HttpException) {
            // 서버 오류 처리
            ResultWrapper.Error(ResultWrapper.ErrorType.ExceptionError("Server error: ${e.code()} - ${e.message()}"))
        } catch (e: Exception) {
            // 알 수 없는 오류 처리
            ResultWrapper.Error(ResultWrapper.ErrorType.ExceptionError("Unexpected error occurred: ${e.localizedMessage}"))
        }
    }
}