package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.FavoriteUser
import com.example.tokitoki.domain.repository.FavoriteUserRepository
import javax.inject.Inject

interface GetFavoriteUsersUseCase {
    suspend operator fun invoke(limit: Int, cursor: Long): List<FavoriteUser>
}

class GetFavoriteUsersUseCaseImpl @Inject constructor(
    private val repository: FavoriteUserRepository
) : GetFavoriteUsersUseCase {
    override suspend operator fun invoke(limit: Int, cursor: Long): List<FavoriteUser> {
        return repository.getFavoriteUsers(limit, cursor)
    }
}