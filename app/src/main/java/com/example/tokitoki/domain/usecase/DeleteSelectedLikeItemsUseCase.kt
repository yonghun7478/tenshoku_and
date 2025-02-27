package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.LikeRepository
import javax.inject.Inject

interface DeleteSelectedLikeItemsUseCase {
    suspend operator fun invoke(itemIds: Set<Int>): Result<Unit>
}

class DeleteSelectedLikeItemsUseCaseImpl @Inject constructor(
    private val repository: LikeRepository
) : DeleteSelectedLikeItemsUseCase {
    override suspend operator fun invoke(itemIds: Set<Int>): Result<Unit> = repository.deleteSelectedLikeItems(itemIds)
}