package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.UserDetail
import com.example.tokitoki.domain.repository.UserCacheRepository
import javax.inject.Inject

interface AddUserDetailToCacheUseCase {
    operator fun invoke(userId: String, userDetail: UserDetail)
}

class AddUserDetailToCacheUseCaseImpl @Inject constructor(
    private val repository: UserCacheRepository
) : AddUserDetailToCacheUseCase {
    override operator fun invoke(userId: String, userDetail: UserDetail) {
        repository.addUserDetailToCache(userId, userDetail)
    }
} 