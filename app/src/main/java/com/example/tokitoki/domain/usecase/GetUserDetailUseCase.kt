package com.example.tokitoki.domain.usecase

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.UserDetail
import com.example.tokitoki.domain.repository.UserRepository
import javax.inject.Inject

interface GetUserDetailUseCase {
    suspend operator fun invoke(userId: String): ResultWrapper<UserDetail>
}

class GetUserDetailUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : GetUserDetailUseCase {
    override suspend operator fun invoke(userId: String): ResultWrapper<UserDetail> {
        return repository.getUserDetail(userId)
    }
} 