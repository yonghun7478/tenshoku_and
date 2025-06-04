package com.example.tokitoki.domain.usecase.tag

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import javax.inject.Inject

interface GetUserTagsUseCase {
    suspend operator fun invoke(userId: String): Result<List<MainHomeTag>>
}

class GetUserTagsUseCaseImpl @Inject constructor(
    private val mainHomeTagRepository: MainHomeTagRepository
) : GetUserTagsUseCase {
    override suspend operator fun invoke(userId: String): Result<List<MainHomeTag>> {
        return mainHomeTagRepository.getUserTags(userId)
    }
} 