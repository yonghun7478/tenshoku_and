package com.example.tokitoki.data.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.data.local.UserEntity
import com.example.tokitoki.domain.converter.UserConverter
import com.example.tokitoki.domain.model.UserList
import com.example.tokitoki.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
) : UserRepository {
    override suspend fun getUsers(cursor: String?, limit: Int, orderBy: String): ResultWrapper<UserList> {
        return try {
            val users =  if(orderBy == "lastLoginAt") {
                listOf(
                    UserEntity(
                        id = "1",
                        thumbnailUrl = "https://cdn.mhnse.com/news/photo/202412/359056_419545_1841.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                    UserEntity(
                        id = "2",
                        thumbnailUrl = "https://cdn.mhnse.com/news/photo/202412/359056_419545_1841.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                    UserEntity(
                        id = "3",
                        thumbnailUrl = "https://cdn.mhnse.com/news/photo/202412/359056_419545_1841.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                    UserEntity(
                        id = "4",
                        thumbnailUrl = "https://cdn.mhnse.com/news/photo/202412/359056_419545_1841.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                    UserEntity(
                        id = "5",
                        thumbnailUrl = "https://cdn.mhnse.com/news/photo/202412/359056_419545_1841.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                    UserEntity(
                        id = "6",
                        thumbnailUrl = "https://cdn.mhnse.com/news/photo/202412/359056_419545_1841.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                    UserEntity(
                        id = "7",
                        thumbnailUrl = "https://cdn.mhnse.com/news/photo/202412/359056_419545_1841.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                    UserEntity(
                        id = "8",
                        thumbnailUrl = "https://cdn.mhnse.com/news/photo/202412/359056_419545_1841.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                    UserEntity(
                        id = "9",
                        thumbnailUrl = "https://cdn.mhnse.com/news/photo/202412/359056_419545_1841.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                    UserEntity(
                        id = "10",
                        thumbnailUrl = "https://cdn.mhnse.com/news/photo/202412/359056_419545_1841.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                )
            } else {
                listOf(
                    UserEntity(
                        id = "1",
                        thumbnailUrl = "https://dimg.donga.com/wps/NEWS/IMAGE/2024/10/23/130275989.1.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                    UserEntity(
                        id = "2",
                        thumbnailUrl = "https://dimg.donga.com/wps/NEWS/IMAGE/2024/10/23/130275989.1.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                    UserEntity(
                        id = "3",
                        thumbnailUrl = "https://dimg.donga.com/wps/NEWS/IMAGE/2024/10/23/130275989.1.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                    UserEntity(
                        id = "4",
                        thumbnailUrl = "https://dimg.donga.com/wps/NEWS/IMAGE/2024/10/23/130275989.1.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                    UserEntity(
                        id = "5",
                        thumbnailUrl = "https://dimg.donga.com/wps/NEWS/IMAGE/2024/10/23/130275989.1.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                    UserEntity(
                        id = "6",
                        thumbnailUrl = "https://dimg.donga.com/wps/NEWS/IMAGE/2024/10/23/130275989.1.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                    UserEntity(
                        id = "7",
                        thumbnailUrl = "https://dimg.donga.com/wps/NEWS/IMAGE/2024/10/23/130275989.1.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                    UserEntity(
                        id = "8",
                        thumbnailUrl = "https://dimg.donga.com/wps/NEWS/IMAGE/2024/10/23/130275989.1.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                    UserEntity(
                        id = "9",
                        thumbnailUrl = "https://dimg.donga.com/wps/NEWS/IMAGE/2024/10/23/130275989.1.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                    UserEntity(
                        id = "10",
                        thumbnailUrl = "https://dimg.donga.com/wps/NEWS/IMAGE/2024/10/23/130275989.1.jpg",
                        age = 22,
                        createdAt = 1234,
                        lastLoginAt = 1234
                    ),
                )
            }

            val nextCursor = "id"
            val isLastPage = false

            ResultWrapper.Success(
                UserList(
                    users = users.map(UserConverter::dataToDomain),
                    nextCursor = nextCursor,
                    isLastPage = isLastPage
                )
            )
        } catch (e: Exception) {
            ResultWrapper.Error(ResultWrapper.ErrorType.ExceptionError(e.message ?: "Unknown error"))
        }
    }
}