package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import javax.inject.Inject

interface GetMyMainHomeTagsUseCase{
    suspend operator fun invoke(): Result<List<MainHomeTag>>
}
class GetMyMainHomeTagsUseCaseImpl @Inject constructor(
    private val repository: MainHomeTagRepository
): GetMyMainHomeTagsUseCase{
    override suspend fun invoke(): Result<List<MainHomeTag>> {
        return repository.getMyTags()
    }
}