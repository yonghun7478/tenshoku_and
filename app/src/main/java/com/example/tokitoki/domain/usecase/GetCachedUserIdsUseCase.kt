package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.UserCacheRepository
import javax.inject.Inject

interface GetCachedUserIdsUseCase {
    operator fun invoke(screenName: String): List<String>
}

class GetCachedUserIdsUseCaseImpl @Inject constructor(
    private val repository: UserCacheRepository
) : GetCachedUserIdsUseCase {
    override operator fun invoke(screenName: String): List<String> {
        return repository.getCachedUserIds(screenName)
    }
}