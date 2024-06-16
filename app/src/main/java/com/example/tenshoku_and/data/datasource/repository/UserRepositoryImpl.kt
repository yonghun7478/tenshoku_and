package com.example.tenshoku_and.data.datasource.repository

import com.example.tenshoku_and.data.datasource.remote.UserApiService
import com.example.tenshoku_and.domain.model.User
import com.example.tenshoku_and.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService,
    ) : UserRepository {
    override suspend fun getRemoteUsers(): List<User> {
        return emptyList()
    }
}