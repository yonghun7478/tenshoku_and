package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MyTag
import com.example.tokitoki.domain.repository.MyProfileRepository
import javax.inject.Inject

// UseCase 인터페이스
interface SetMyTagUseCase {
    suspend operator fun invoke(tags: List<MyTag>): Result<List<MyTag>>
}

// UseCase 구현 클래스
class SetMyTagUseCaseImpl @Inject constructor(
    private val repository: MyProfileRepository
) : SetMyTagUseCase {
    override suspend operator fun invoke(tags: List<MyTag>): Result<List<MyTag>> {
        return try {
            // 1. 태그 저장
            tags.forEach { repository.addUserTag(it) }

            // 2. 저장된 태그 목록 가져오기
            val savedTags = repository.getUserTags()

            Result.success(savedTags)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
