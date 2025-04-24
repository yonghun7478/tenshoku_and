package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.LikedUser
import com.example.tokitoki.domain.repository.LikedUserRepository
import javax.inject.Inject

interface GetLikedUsersUseCase {
    suspend operator fun invoke(cursor: Long? = null, pageSize: Int = 20): List<LikedUser>
}

class GetLikedUsersUseCaseImpl @Inject constructor(
    private val repository: LikedUserRepository
) : GetLikedUsersUseCase {
    override suspend operator fun invoke(cursor: Long?, pageSize: Int): List<LikedUser> {
        return repository.getLikedUsers(cursor, pageSize)
    }
} 