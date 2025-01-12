package com.example.tokitoki.data.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.data.local.UserEntity
import com.example.tokitoki.domain.converter.UserConverter
import com.example.tokitoki.domain.model.UserList
import com.example.tokitoki.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
) : UserRepository {
    override suspend fun getUsers(
        cursor: String?,
        limit: Int,
        orderBy: String
    ): ResultWrapper<UserList> {
        return try {
            val users = if (orderBy == "lastLoginAt") {
                (1..30).map { id ->
                    UserEntity(
                        id = id.toString(),
                        thumbnailUrl = "https://cdn.mhnse.com/news/photo/202412/359056_419545_1841.jpg",
                        age = (18..60).random(),
                        createdAt = (1609459200..1672531199).random()
                            .toLong(), // 2021-01-01 to 2022-12-31 Unix timestamp range
                        lastLoginAt = (1672531200..1704067199).random()
                            .toLong() // 2023-01-01 to 2023-12-31 Unix timestamp range
                    )
                }
            } else {
                (1..30).map { id ->
                    UserEntity(
                        id = id.toString(),
                        thumbnailUrl = "https://dimg.donga.com/wps/NEWS/IMAGE/2024/10/23/130275989.1.jpg",
                        age = (18..60).random(),
                        createdAt = (1609459200..1672531199).random()
                            .toLong(), // 2021-01-01 to 2022-12-31 Unix timestamp range
                        lastLoginAt = (1672531200..1704067199).random()
                            .toLong() // 2023-01-01 to 2023-12-31 Unix timestamp range
                    )
                }
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
            ResultWrapper.Error(
                ResultWrapper.ErrorType.ExceptionError(
                    e.message ?: "Unknown error"
                )
            )
        }
    }
}