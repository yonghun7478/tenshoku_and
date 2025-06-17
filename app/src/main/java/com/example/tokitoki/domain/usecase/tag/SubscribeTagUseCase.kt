package com.example.tokitoki.domain.usecase.tag

import com.example.tokitoki.domain.repository.TagRepository
import javax.inject.Inject

interface SubscribeTagUseCase {
    suspend operator fun invoke(tagId: String): Result<Unit>
}

class SubscribeTagUseCaseImpl @Inject constructor(
    private val tagRepository: TagRepository
) : SubscribeTagUseCase {
    override suspend fun invoke(tagId: String): Result<Unit> {
        return tagRepository.subscribeTag(tagId)
    }
} 