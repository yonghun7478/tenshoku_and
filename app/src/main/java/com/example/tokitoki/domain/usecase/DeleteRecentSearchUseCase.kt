package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import javax.inject.Inject

interface DeleteRecentSearchUseCase{
    suspend operator fun invoke(tag: MainHomeTag): Result<Unit>
}
class DeleteRecentSearchUseCaseImpl @Inject constructor(
    private val repository: MainHomeTagRepository
): DeleteRecentSearchUseCase{
    override suspend fun invoke(tag: MainHomeTag): Result<Unit> {
        return repository.deleteRecentSearch(tag)
    }
}