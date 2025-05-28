package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import javax.inject.Inject

interface GetTagDetailUseCase {
    suspend operator fun invoke(tagId: String): Result<MainHomeTag>
}

class GetTagDetailUseCaseImpl @Inject constructor(
    private val mainHomeTagRepository: MainHomeTagRepository
) : GetTagDetailUseCase {
    override suspend operator fun invoke(tagId: String): Result<MainHomeTag> {
        return mainHomeTagRepository.getTagDetail(tagId)
    }
} 