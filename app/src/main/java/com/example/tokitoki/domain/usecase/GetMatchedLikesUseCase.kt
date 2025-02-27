package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.LikeItem
import com.example.tokitoki.domain.repository.LikeRepository
import javax.inject.Inject

interface GetMatchedLikesUseCase{
    suspend operator fun invoke(): Result<List<LikeItem>>
}

class GetMatchedLikesUseCaseImpl @Inject constructor(
    private val repository: LikeRepository
) : GetMatchedLikesUseCase {
    override suspend operator fun invoke(): Result<List<LikeItem>> = repository.getMatchedLikes()
}