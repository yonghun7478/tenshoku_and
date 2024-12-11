package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.repository.MyProfileRepository
import javax.inject.Inject

// UseCase 인터페이스
interface SetNameUseCase {
    suspend operator fun invoke(name: String): Result<MyProfile>
}

// UseCase 구현 클래스
class SetNameUseCaseImpl @Inject constructor(
    private val repository: MyProfileRepository
) : SetNameUseCase {
    override suspend operator fun invoke(name: String): Result<MyProfile> {
        return try {
            repository.updateUserName(name) // 이름 업데이트
            val updatedProfile = repository.getUserProfile() // 업데이트된 프로필 가져오기
            if (updatedProfile != null) {
                Result.success(updatedProfile) // 성공적으로 프로필 반환
            } else {
                Result.failure(IllegalStateException("Failed to fetch updated profile"))
            }
        } catch (e: Exception) {
            Result.failure(e) // 실패 시 에러 반환
        }
    }
}
