package com.example.tenshoku_and.domain.repository

import com.example.tenshoku_and.domain.model.User

interface UserRepository {
    suspend fun getRemoteUsers():List<User>
}