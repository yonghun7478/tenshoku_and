package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.LikeItem
import com.example.tokitoki.domain.repository.LikeRepository
import javax.inject.Inject

interface LoadMoreLikesUseCase{
    suspend operator fun invoke(tab: String, startIndex: Int): Result<List<LikeItem>>
}

class LoadMoreLikesUseCaseImpl @Inject constructor(
    private val repository: LikeRepository
) : LoadMoreLikesUseCase {
    override suspend operator fun invoke(tab: String, startIndex: Int): Result<List<LikeItem>> =
        repository.loadMoreLikes(tab, startIndex)
}