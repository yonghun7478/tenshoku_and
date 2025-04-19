package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.FavoriteUser

interface FavoriteUserRepository {
    suspend fun getFavoriteUsers(limit: Int, cursor: Long): List<FavoriteUser>
}