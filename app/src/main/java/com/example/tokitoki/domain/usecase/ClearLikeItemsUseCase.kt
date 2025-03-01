package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.LikeRepository
import javax.inject.Inject

interface ClearLikeItemsUseCase {
    suspend operator fun invoke(tab: String): Result<Unit>
}

class ClearLikeItemsUseCaseImpl @Inject constructor(
    private val repository: LikeRepository
) : ClearLikeItemsUseCase {
    override suspend operator fun invoke(tab: String): Result<Unit> = repository.clearLikeItem(tab)
}