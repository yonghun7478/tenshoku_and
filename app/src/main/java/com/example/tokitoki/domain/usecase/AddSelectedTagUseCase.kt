package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import javax.inject.Inject

interface AddSelectedTagUseCase{
    suspend operator fun invoke(tag: MainHomeTag): Result<Unit>
}
class AddSelectedTagUseCaseImpl @Inject constructor(
    private val repository: MainHomeTagRepository
) : AddSelectedTagUseCase {
    override suspend fun invoke(tag: MainHomeTag): Result<Unit> {
        return repository.addSelectedTag(tag)
    }
}