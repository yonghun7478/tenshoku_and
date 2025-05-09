package com.example.tokitoki.data.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.data.local.UserEntity
import com.example.tokitoki.domain.converter.UserConverter
import com.example.tokitoki.domain.model.UserList
import com.example.tokitoki.domain.repository.UserRepository
import javax.inject.Inject
import android.util.LruCache
import com.example.tokitoki.domain.model.UserDetail
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
) : UserRepository {
    // 150개 더미 데이터 생성 (id는 1~150)
    private val dummyUsers: List<UserEntity> = (1..150).map { id ->
        UserEntity(
            id = id.toString(),
            thumbnailUrl = if (id % 2 == 0)
                "https://cdn.mhnse.com/news/photo/202412/359056_419545_1841.jpg"
            else
                "https://dimg.donga.com/wps/NEWS/IMAGE/2024/10/23/130275989.1.jpg",
            age = (18..60).random(),
            createdAt = (1609459200..1672531199).random().toLong(),
            lastLoginAt = (1672531200..1704067199).random().toLong()
        )
    }

    // 반환된 유저 id를 정렬 기준별로 캐싱할 리스트
    private val cachedUserIdsByLastLogin: MutableList<String> = mutableListOf()
    private val cachedUserIdsByCreatedAt: MutableList<String> = mutableListOf()

    // UserDetail 캐시 (id -> UserDetail)
    private val userDetailCache = LruCache<String, UserDetail>(100)

    override suspend fun getUsers(
        cursor: String?,
        limit: Int,
        orderBy: String
    ): ResultWrapper<UserList> {
        return try {
            // 정렬
            val sortedUsers = when (orderBy) {
                "lastLoginAt" -> dummyUsers.sortedByDescending { it.lastLoginAt }
                else -> dummyUsers.sortedByDescending { it.createdAt }
            }

            // cursor가 null이면 처음부터, 아니면 해당 id 이후부터
            val startIndex = cursor?.let { c ->
                sortedUsers.indexOfFirst { it.id == c } + 1
            } ?: 0

            val pagedUsers = sortedUsers.drop(startIndex).take(limit)

            // orderBy에 따라 thumbnailUrl 변경
            val thumbnailUrl = if (orderBy == "lastLoginAt")
                "https://cdn.mhnse.com/news/photo/202412/359056_419545_1841.jpg"
            else
                "https://dimg.donga.com/wps/NEWS/IMAGE/2024/10/23/130275989.1.jpg"

            val mappedUsers = pagedUsers.map { it.copy(thumbnailUrl = thumbnailUrl) }

            // 정렬 기준에 따라 각각의 리스트에 캐싱
            if (orderBy == "lastLoginAt") {
                cachedUserIdsByLastLogin.addAll(mappedUsers.map { it.id })
            } else {
                cachedUserIdsByCreatedAt.addAll(mappedUsers.map { it.id })
            }

            val nextCursor = mappedUsers.lastOrNull()?.id
            val isLastPage = (startIndex + mappedUsers.size) >= sortedUsers.size

            ResultWrapper.Success(
                UserList(
                    users = mappedUsers.map(UserConverter::dataToDomain),
                    nextCursor = if (isLastPage) null else nextCursor,
                    isLastPage = isLastPage
                )
            )
        } catch (e: Exception) {
            ResultWrapper.Error(
                ResultWrapper.ErrorType.ExceptionError(
                    e.message ?: "Unknown error"
                )
            )
        }
    }

    override suspend fun getUserDetail(userId: String, orderBy: String): ResultWrapper<UserDetail> {
        return try {
            // 1. LruCache에서 먼저 조회
            val cached = userDetailCache.get(userId)
            if (cached != null) {
                return ResultWrapper.Success(cached)
            }
            // 2. 없으면 dummyUsers에서 조회
            val user = dummyUsers.find { it.id == userId }
            if (user != null) {
                // orderBy에 따라 thumbnailUrl 설정
                val thumbnailUrl = if (orderBy == "lastLoginAt")
                    "https://cdn.mhnse.com/news/photo/202412/359056_419545_1841.jpg"
                else
                    "https://dimg.donga.com/wps/NEWS/IMAGE/2024/10/23/130275989.1.jpg"

                val detail = UserDetail(
                    id = user.id,
                    name = "User${user.id}",
                    birthDay = "1990-01-01",
                    isMale = user.id.toIntOrNull()?.rem(2) == 0,
                    mySelfSentenceId = 0,
                    email = "user${user.id}@example.com",
                    thumbnailUrl = thumbnailUrl
                )
                // 3. 캐시에 저장
                userDetailCache.put(userId, detail)
                ResultWrapper.Success(detail)
            } else {
                ResultWrapper.Error(ResultWrapper.ErrorType.ExceptionError("User not found"))
            }
        } catch (e: Exception) {
            ResultWrapper.Error(ResultWrapper.ErrorType.ExceptionError(e.message ?: "Unknown error"))
        }
    }

    override fun clearCachedUserIds(orderBy: String?) {
        when (orderBy) {
            "lastLoginAt" -> cachedUserIdsByLastLogin.clear()
            "createdAt" -> cachedUserIdsByCreatedAt.clear()
            null -> {
                cachedUserIdsByLastLogin.clear()
                cachedUserIdsByCreatedAt.clear()
            }
        }
    }

    override fun getCachedUserIds(orderBy: String): List<String> {
        return when (orderBy) {
            "lastLoginAt" -> cachedUserIdsByLastLogin.toList()
            "createdAt" -> cachedUserIdsByCreatedAt.toList()
            else -> emptyList()
        }
    }
}