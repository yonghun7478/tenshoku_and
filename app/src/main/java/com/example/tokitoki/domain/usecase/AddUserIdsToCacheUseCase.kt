package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.UserCacheRepository
import javax.inject.Inject

interface AddUserIdsToCacheUseCase {
    operator fun invoke(screenName: String, userIds: List<String>)
}

class AddUserIdsToCacheUseCaseImpl @Inject constructor(
    private val repository: UserCacheRepository
) : AddUserIdsToCacheUseCase {
    override operator fun invoke(screenName: String, userIds: List<String>) {
        repository.addUserIdsToCache(screenName, userIds)
    }
} 