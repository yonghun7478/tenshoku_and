package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.LikeItem
import com.example.tokitoki.domain.repository.LikeRepository
import javax.inject.Inject

interface GetSentLikesUseCase {
    suspend operator fun invoke(): Result<List<LikeItem>>
}

class GetSentLikesUseCaseImpl @Inject constructor(
    private val repository: LikeRepository
) : GetSentLikesUseCase {
    override suspend operator fun invoke(): Result<List<LikeItem>> = repository.getSentLikes()
}