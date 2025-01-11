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
            val users = listOf(
                UserEntity(
                    id = "1",
                    thumbnailUrl = "asdf",
                    age = 12,
                    createdAt = 1234,
                    lastLoginAt = 1234
                )
            )
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