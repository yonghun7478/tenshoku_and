package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import javax.inject.Inject

interface GetTrendingTagsUseCase {
    suspend operator fun invoke(): Result<List<MainHomeTag>>
}

class GetTrendingTagsUseCaseImpl @Inject constructor(
    private val repository: MainHomeTagRepository
) : GetTrendingTagsUseCase {
    override suspend fun invoke(): Result<List<MainHomeTag>> {
        return repository.getTrendingTags()
    }
}