package com.example.tokitoki.domain.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.UserList
import com.example.tokitoki.domain.model.UserDetail

interface UserRepository {
    suspend fun getUsers(cursor: String?, limit: Int, orderBy: String): ResultWrapper<UserList>

    // 유저 상세 데이터 조회
    suspend fun getUserDetail(userId: String): ResultWrapper<UserDetail>

    // id 리스트로 유저 id 리스트 조회 (없으면 null)
    suspend fun getUsersByIds(userIds: List<String>): List<String>?

    // 유저 id 캐시 초기화
    fun clearCachedUserIds()
}
