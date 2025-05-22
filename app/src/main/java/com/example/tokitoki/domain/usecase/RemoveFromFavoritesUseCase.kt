package com.example.tokitoki.domain.usecase

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.repository.FavoriteUserRepository
import javax.inject.Inject

interface RemoveFromFavoritesUseCase {
    suspend operator fun invoke(userId: String): ResultWrapper<Unit>
}

class RemoveFromFavoritesUseCaseImpl @Inject constructor(
    private val repository: FavoriteUserRepository
) : RemoveFromFavoritesUseCase {
    override suspend fun invoke(userId: String): ResultWrapper<Unit> {
        return repository.removeFromFavorites(userId)
    }
} 