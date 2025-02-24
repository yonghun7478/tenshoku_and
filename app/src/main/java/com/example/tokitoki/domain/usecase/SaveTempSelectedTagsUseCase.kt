package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import javax.inject.Inject

interface SaveTempSelectedTagsUseCase{
    suspend operator fun invoke(tags: List<MainHomeTag>)
}
class SaveTempSelectedTagsUseCaseImpl @Inject constructor(
    private val repository: MainHomeTagRepository
): SaveTempSelectedTagsUseCase{
    override suspend fun invoke(tags: List<MainHomeTag>){
        return repository.saveTempSelectedTags(tags)
    }
}