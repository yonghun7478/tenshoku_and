package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import javax.inject.Inject

interface SearchTagsUseCase{
    suspend operator fun invoke(query: String): Result<List<MainHomeTag>>
}
class SearchTagsUseCaseImpl @Inject constructor(
    private val repository: MainHomeTagRepository
) : SearchTagsUseCase {
    override suspend fun invoke(query: String): Result<List<MainHomeTag>> {
        return repository.searchTags(query)
    }
}