package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.MyProfileRepository
import javax.inject.Inject

interface UnsubscribeTagUseCase {
    suspend operator fun invoke(tagId: String): Result<Unit>
}

class UnsubscribeTagUseCaseImpl @Inject constructor(
    private val myProfileRepository: MyProfileRepository
) : UnsubscribeTagUseCase {
    override suspend operator fun invoke(tagId: String): Result<Unit> {
        return myProfileRepository.unsubscribeTag(tagId)
    }
} 