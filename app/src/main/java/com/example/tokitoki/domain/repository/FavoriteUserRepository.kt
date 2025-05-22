package com.example.tokitoki.domain.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.FavoriteUser

interface FavoriteUserRepository {
    suspend fun getFavoriteUsers(limit: Int, cursor: Long): List<FavoriteUser>
    suspend fun addToFavorites(userId: String): ResultWrapper<Unit>
    suspend fun removeFromFavorites(userId: String): ResultWrapper<Unit>
}