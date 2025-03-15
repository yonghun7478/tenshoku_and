package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.LikeResult
import com.example.tokitoki.domain.repository.LikeRepository
import javax.inject.Inject

interface GetLikesUseCase {
    suspend operator fun invoke(tab: String, cursor: Long? = null, limit: Int = 20): Result<LikeResult>
}

class GetLikesUseCaseImpl @Inject constructor(
    private val repository: LikeRepository
) : GetLikesUseCase {
    override suspend operator fun invoke(tab: String, cursor: Long?, limit: Int): Result<LikeResult> =
        repository.getLikes(tab, cursor, limit)
}