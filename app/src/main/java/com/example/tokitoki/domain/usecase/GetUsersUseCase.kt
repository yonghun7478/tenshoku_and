package com.example.tokitoki.domain.usecase

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.UserList
import com.example.tokitoki.domain.repository.UserRepository
import javax.inject.Inject

interface GetUsersUseCase {
    suspend fun execute(cursor: String?, limit: Int): ResultWrapper<UserList>
}

class GetUsersByLoginUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : GetUsersUseCase {
    override suspend fun execute(cursor: String?, limit: Int): ResultWrapper<UserList> {
        return repository.getUsers(cursor, limit, "lastLoginAt")
    }
}

class GetUsersBySignupUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : GetUsersUseCase {
    override suspend fun execute(cursor: String?, limit: Int): ResultWrapper<UserList> {
        return repository.getUsers(cursor, limit, "createdAt")
    }
}