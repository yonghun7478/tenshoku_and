package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.LikeItem
import com.example.tokitoki.domain.repository.LikeRepository
import javax.inject.Inject

interface GetReceivedLikesUseCase {
    suspend operator fun invoke(): Result<List<LikeItem>>
}

// 구현 클래스
class GetReceivedLikesUseCaseImpl @Inject constructor(
    private val repository: LikeRepository
) : GetReceivedLikesUseCase {
    override suspend operator fun invoke(): Result<List<LikeItem>> = repository.getReceivedLikes()
}