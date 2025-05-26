package com.example.tokitoki.domain.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.UserList
import com.example.tokitoki.domain.model.UserDetail

interface UserRepository {
    suspend fun getUsers(cursor: String?, limit: Int, orderBy: String): ResultWrapper<UserList>

    // 유저 상세 데이터 조회
    suspend fun getUserDetail(userId: String): ResultWrapper<UserDetail>

    // みてね 전송
    suspend fun sendMiten(userId: String): ResultWrapper<Unit>
    
    // 태그 구독자 목록 조회 (항상 lastLoginAt 기준으로 정렬)
    suspend fun getTagSubscribers(tagId: String, cursor: String?, limit: Int): ResultWrapper<UserList>
}