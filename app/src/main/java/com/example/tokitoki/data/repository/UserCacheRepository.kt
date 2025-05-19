package com.example.tokitoki.data.repository

import android.util.LruCache
import com.example.tokitoki.domain.model.UserDetail
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserCacheRepository @Inject constructor() {
    // 반환된 유저 id를 화면별로 캐싱할 맵
    private val cachedUserIdsMap: MutableMap<String, MutableList<String>> = mutableMapOf()

    // UserDetail 캐시 (id -> UserDetail)
    private val userDetailCache = LruCache<String, UserDetail>(100)

    fun clearCachedUserIds(screenName: String?) {
        if (screenName != null) {
            cachedUserIdsMap.remove(screenName)
        } else {
            cachedUserIdsMap.clear()
        }
    }

    fun getCachedUserIds(screenName: String): List<String> {
        return cachedUserIdsMap[screenName]?.toList() ?: emptyList()
    }

    fun addUserIdsToCache(screenName: String, userIds: List<String>) {
        val existingList = cachedUserIdsMap.getOrPut(screenName) { mutableListOf() }
        existingList.addAll(userIds)
    }

    fun addUserDetailToCache(userId: String, userDetail: UserDetail) {
        userDetailCache.put(userId, userDetail)
    }

    fun getUserDetailFromCache(userId: String): UserDetail? {
        return userDetailCache.get(userId)
    }
} 