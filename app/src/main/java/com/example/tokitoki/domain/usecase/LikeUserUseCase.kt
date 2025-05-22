package com.example.tokitoki.domain.usecase

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.repository.LikeRepository
import javax.inject.Inject

interface LikeUserUseCase {
    suspend operator fun invoke(userId: String): ResultWrapper<Unit>
}

class LikeUserUseCaseImpl @Inject constructor(
    private val repository: LikeRepository
) : LikeUserUseCase {
    override suspend fun invoke(userId: String): ResultWrapper<Unit> {
        return repository.likeUser(userId)
    }
} 