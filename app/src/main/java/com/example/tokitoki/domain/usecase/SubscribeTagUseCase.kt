package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.MyProfileRepository
import javax.inject.Inject

interface SubscribeTagUseCase {
    suspend operator fun invoke(tagId: String): Result<Unit>
}

class SubscribeTagUseCaseImpl @Inject constructor(
    private val myProfileRepository: MyProfileRepository
) : SubscribeTagUseCase {
    override suspend operator fun invoke(tagId: String): Result<Unit> {
        return myProfileRepository.subscribeTag(tagId)
    }
} 