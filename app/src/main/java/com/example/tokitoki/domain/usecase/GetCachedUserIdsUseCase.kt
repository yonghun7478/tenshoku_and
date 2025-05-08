package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.UserRepository
import javax.inject.Inject

interface GetCachedUserIdsUseCase {
    operator fun invoke(orderBy: String): List<String>
}

class GetCachedUserIdsUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : GetCachedUserIdsUseCase {
    override operator fun invoke(orderBy: String): List<String> {
        return repository.getCachedUserIds(orderBy)
    }
}