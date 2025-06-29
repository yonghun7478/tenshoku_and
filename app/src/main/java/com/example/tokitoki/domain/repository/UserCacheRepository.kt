package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.UserDetail

interface UserCacheRepository {
    fun clearCachedUserIds(screenName: String?)
    fun getCachedUserIds(screenName: String): List<String>
    fun addUserIdsToCache(screenName: String, userIds: List<String>)
    fun addUserDetailToCache(userId: String, userDetail: UserDetail)
    fun getUserDetailFromCache(userId: String): UserDetail?
} 