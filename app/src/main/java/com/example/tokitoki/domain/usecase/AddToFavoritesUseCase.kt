package com.example.tokitoki.domain.usecase

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.repository.FavoriteUserRepository
import javax.inject.Inject

interface AddToFavoritesUseCase {
    suspend operator fun invoke(userId: String): ResultWrapper<Unit>
}

class AddToFavoritesUseCaseImpl @Inject constructor(
    private val repository: FavoriteUserRepository
) : AddToFavoritesUseCase {
    override suspend fun invoke(userId: String): ResultWrapper<Unit> {
        return repository.addToFavorites(userId)
    }
} 