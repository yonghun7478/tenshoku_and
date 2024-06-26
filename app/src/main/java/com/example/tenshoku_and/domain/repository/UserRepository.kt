package com.example.tenshoku_and.domain.repository

import com.example.tenshoku_and.domain.model.User
import com.example.tenshoku_and.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsersFromApi(): Flow<Result<List<User>>>
    suspend fun saveUsersToDb(users: List<User>)
}