package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import javax.inject.Inject

interface RestoreTempSelectedTagsUseCase{
    suspend operator fun invoke() : Result<List<MainHomeTag>>
}
class RestoreTempSelectedTagsUseCaseImpl @Inject constructor(
    private val repository: MainHomeTagRepository
):RestoreTempSelectedTagsUseCase{
    override suspend fun invoke() : Result<List<MainHomeTag>>{
        return repository.restoreTempSelectedTags()
    }
}