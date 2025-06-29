package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.UserDetail
import com.example.tokitoki.domain.repository.UserCacheRepository
import javax.inject.Inject

interface GetUserDetailFromCacheUseCase {
    operator fun invoke(userId: String): UserDetail?
}

class GetUserDetailFromCacheUseCaseImpl @Inject constructor(
    private val repository: UserCacheRepository
) : GetUserDetailFromCacheUseCase {
    override operator fun invoke(userId: String): UserDetail? {
        return repository.getUserDetailFromCache(userId)
    }
} 