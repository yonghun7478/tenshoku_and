package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.LikedUserRepository
import javax.inject.Inject

interface UpdateLikeStatusUseCase {
    suspend operator fun invoke(userId: String, isLiked: Boolean): Result<Unit>
}

class UpdateLikeStatusUseCaseImpl @Inject constructor(
    private val repository: LikedUserRepository
) : UpdateLikeStatusUseCase {
    override suspend operator fun invoke(userId: String, isLiked: Boolean): Result<Unit> {
        return repository.updateLikeStatus(userId, isLiked)
    }
} 