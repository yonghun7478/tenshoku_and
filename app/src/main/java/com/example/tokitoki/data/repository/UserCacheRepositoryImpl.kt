package com.example.tokitoki.data.repository

import android.util.LruCache
import com.example.tokitoki.domain.model.UserDetail
import com.example.tokitoki.domain.repository.UserCacheRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserCacheRepositoryImpl @Inject constructor() : UserCacheRepository {
    // 반환된 유저 id를 화면별로 캐싱할 맵
    private val cachedUserIdsMap: MutableMap<String, MutableList<String>> = mutableMapOf()

    // UserDetail 캐시 (id -> UserDetail)
    private val userDetailCache = LruCache<String, UserDetail>(100)

    override fun clearCachedUserIds(screenName: String?) {
        if (screenName != null) {
            cachedUserIdsMap.remove(screenName)
        } else {
            cachedUserIdsMap.clear()
        }
    }

    override fun getCachedUserIds(screenName: String): List<String> {
        return cachedUserIdsMap[screenName]?.toList() ?: emptyList()
    }

    override fun addUserIdsToCache(screenName: String, userIds: List<String>) {
        // 기존 데이터를 모두 지우고 새로운 데이터로 교체
        cachedUserIdsMap[screenName] = userIds.toMutableList()
    }

    override fun addUserDetailToCache(userId: String, userDetail: UserDetail) {
        userDetailCache.put(userId, userDetail)
    }

    override fun getUserDetailFromCache(userId: String): UserDetail? {
        return userDetailCache.get(userId)
    }
} 