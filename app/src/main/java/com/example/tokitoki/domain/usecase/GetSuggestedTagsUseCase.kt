package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import javax.inject.Inject

interface GetSuggestedTagsUseCase{
    suspend operator fun invoke(): Result<List<MainHomeTag>>
}
class GetSuggestedTagsUseCaseImpl @Inject constructor(
    private val repository: MainHomeTagRepository
) : GetSuggestedTagsUseCase {
    override suspend fun invoke(): Result<List<MainHomeTag>> {
        return repository.getSuggestedTags()
    }
}