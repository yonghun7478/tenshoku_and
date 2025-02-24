package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import javax.inject.Inject

interface RemoveSelectedTagUseCase{
    suspend operator fun invoke(tag: MainHomeTag): Result<Unit>
}
class RemoveSelectedTagUseCaseImpl @Inject constructor(
    private val repository: MainHomeTagRepository
) : RemoveSelectedTagUseCase{
    override suspend fun invoke(tag: MainHomeTag): Result<Unit> {
        return repository.removeSelectedTag(tag)
    }
}