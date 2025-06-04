package com.example.tokitoki.domain.usecase

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.repository.LikeRepository
import javax.inject.Inject

interface CheckIsUserLikedUseCase {
    suspend operator fun invoke(userId: String): ResultWrapper<Boolean>
}

class CheckIsUserLikedUseCaseImpl @Inject constructor(
    private val likeRepository: LikeRepository
) : CheckIsUserLikedUseCase {
    override suspend fun invoke(userId: String): ResultWrapper<Boolean> {
        return likeRepository.isUserLiked(userId)
    }
} 