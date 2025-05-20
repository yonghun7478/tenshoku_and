package com.example.tokitoki.domain.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.LikeResult

interface LikeRepository {
    suspend fun getLikes(tab: String, cursor: Long? = null, limit: Int = 20): Result<LikeResult>
    suspend fun likeUser(userId: String): ResultWrapper<Unit>
}