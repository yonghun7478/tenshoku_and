package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.UserCacheRepository
import javax.inject.Inject

interface ClearCachedUserIdsUseCase {
    operator fun invoke(screenName: String? = null)
}

class ClearCachedUserIdsUseCaseImpl @Inject constructor(
    private val repository: UserCacheRepository
) : ClearCachedUserIdsUseCase {
    override operator fun invoke(screenName: String?) {
        repository.clearCachedUserIds(screenName)
    }
} 