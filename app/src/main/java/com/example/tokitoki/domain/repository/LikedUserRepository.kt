package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.LikedUser

interface LikedUserRepository {
    /**
     * 좋아요를 누른 사용자 목록을 가져옵니다.
     * @param cursor 마지막으로 가져온 항목의 좋아요 시간. null인 경우 첫 페이지를 가져옵니다.
     * @param pageSize 한 번에 가져올 항목의 수
     * @return List<LikedUser> 좋아요를 누른 사용자 목록
     */
    suspend fun getLikedUsers(cursor: Long? = null, pageSize: Int = 20): List<LikedUser>

    /**
     * 특정 사용자의 좋아요 상태를 업데이트합니다.
     * @param userId 사용자 ID
     * @param isLiked 좋아요 상태
     */
    suspend fun updateLikeStatus(userId: String, isLiked: Boolean): Result<Unit>
} 