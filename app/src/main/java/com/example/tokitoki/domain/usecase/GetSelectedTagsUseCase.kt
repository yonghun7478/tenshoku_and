package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import javax.inject.Inject

interface GetSelectedTagsUseCase{
    suspend operator fun invoke(): Result<List<MainHomeTag>>
}
class GetSelectedTagsUseCaseImpl @Inject constructor(
    private val repository: MainHomeTagRepository
) : GetSelectedTagsUseCase{
    override suspend fun invoke(): Result<List<MainHomeTag>> {
        return repository.getSelectedTags()
    }
}