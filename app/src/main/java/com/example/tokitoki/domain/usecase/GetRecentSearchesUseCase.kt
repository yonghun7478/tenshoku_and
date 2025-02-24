package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import javax.inject.Inject

interface GetRecentSearchesUseCase{
    suspend operator fun invoke() : Result<List<MainHomeTag>>
}
class GetRecentSearchesUseCaseImpl @Inject constructor(
    private val repository: MainHomeTagRepository
): GetRecentSearchesUseCase{
    override suspend fun invoke() : Result<List<MainHomeTag>>{
        return repository.getRecentSearches()
    }
}