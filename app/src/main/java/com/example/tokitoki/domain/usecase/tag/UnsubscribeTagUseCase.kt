package com.example.tokitoki.domain.usecase.tag

import com.example.tokitoki.domain.repository.TagRepository
import javax.inject.Inject

interface UnsubscribeTagUseCase {
    suspend operator fun invoke(tagId: String): Result<Unit>
}

class UnsubscribeTagUseCaseImpl @Inject constructor(
    private val tagRepository: TagRepository
) : UnsubscribeTagUseCase {
    override suspend fun invoke(tagId: String): Result<Unit> {
        return tagRepository.unsubscribeTag(tagId)
    }
} 