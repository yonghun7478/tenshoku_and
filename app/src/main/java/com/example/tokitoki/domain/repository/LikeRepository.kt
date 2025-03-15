package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.LikeResult

interface LikeRepository {
    suspend fun getLikes(tab: String, cursor: Long? = null, limit: Int = 20): Result<LikeResult> // 반환 타입 변경
}