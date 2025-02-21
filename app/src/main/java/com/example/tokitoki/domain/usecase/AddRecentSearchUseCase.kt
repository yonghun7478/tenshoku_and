package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import javax.inject.Inject

interface AddRecentSearchUseCase{
    suspend operator fun invoke(tag: MainHomeTag) : Result<Unit>
}
class AddRecentSearchUseCaseImpl @Inject constructor(
    private val repository: MainHomeTagRepository
): AddRecentSearchUseCase{
    override suspend fun invoke(tag: MainHomeTag) : Result<Unit>{
        return repository.addRecentSearch(tag)
    }
}