package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.LikeRepository
import javax.inject.Inject

interface DeleteLikeItemUseCase {
    suspend operator fun invoke(itemId: Int): Result<Unit>
}

class DeleteLikeItemUseCaseImpl @Inject constructor(
    private val repository: LikeRepository
) : DeleteLikeItemUseCase {
    override suspend operator fun invoke(itemId: Int): Result<Unit> = repository.deleteLikeItem(itemId)
}