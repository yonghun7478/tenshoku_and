package com.example.tokitoki.data.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.data.local.UserEntity
import com.example.tokitoki.domain.converter.UserConverter
import com.example.tokitoki.domain.model.UserList
import com.example.tokitoki.domain.repository.UserRepository
import javax.inject.Inject

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
}