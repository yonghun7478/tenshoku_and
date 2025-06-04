package com.example.tokitoki.domain.usecase

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.repository.FavoriteUserRepository
import javax.inject.Inject

interface CheckIsUserFavoriteUseCase {
    suspend operator fun invoke(userId: String): ResultWrapper<Boolean>
}

class CheckIsUserFavoriteUseCaseImpl @Inject constructor(
    private val favoriteUserRepository: FavoriteUserRepository
) : CheckIsUserFavoriteUseCase {
    override suspend fun invoke(userId: String): ResultWrapper<Boolean> {
        return favoriteUserRepository.isUserFavorite(userId)
    }
} 