package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.UserRepository
import javax.inject.Inject

interface ClearCachedUserIdsUseCase {
    operator fun invoke(orderBy: String? = null)
}

class ClearCachedUserIdsUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : ClearCachedUserIdsUseCase {
    override operator fun invoke(orderBy: String?) {
        repository.clearCachedUserIds(orderBy)
    }
} 