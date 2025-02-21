package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import javax.inject.Inject

interface GetTodayTagUseCase {
    suspend operator fun invoke(): Result<MainHomeTag>
}

class GetTodayTagUseCaseImpl @Inject constructor(
    private val repository: MainHomeTagRepository
) : GetTodayTagUseCase {
    override suspend fun invoke(): Result<MainHomeTag> {
        return repository.getTodayTag()
    }
}