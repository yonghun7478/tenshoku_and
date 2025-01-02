package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.User
import com.example.tokitoki.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsersFromApi(): Flow<Resource<List<User>>>
    suspend fun getUsersFromDb(): Flow<Resource<List<User>>>
    suspend fun saveUsersToDb(users: List<User>)
    suspend fun saveUserToDb(user: User)
    suspend fun deleteUserFromDb(id: Int)
    suspend fun updateUserFromDb(id: Int, name: String)
    suspend fun saveUserNameFromPreferences(name: String)
    suspend fun getUserNameFromPreferences(): String?
}