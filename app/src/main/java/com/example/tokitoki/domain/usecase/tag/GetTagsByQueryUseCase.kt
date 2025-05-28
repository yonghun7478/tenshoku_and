package com.example.tokitoki.domain.usecase.tag

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import javax.inject.Inject

interface GetTagsByQueryUseCase {
    suspend operator fun invoke(query: String): Result<List<MainHomeTag>>
}

class GetTagsByQueryUseCaseImpl @Inject constructor(
    private val mainHomeTagRepository: MainHomeTagRepository
) : GetTagsByQueryUseCase {
    override suspend fun invoke(query: String): Result<List<MainHomeTag>> {
        return mainHomeTagRepository.getTagsByQuery(query)
    }
} 