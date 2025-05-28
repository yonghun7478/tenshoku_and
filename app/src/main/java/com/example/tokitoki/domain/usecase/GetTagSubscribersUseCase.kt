package com.example.tokitoki.domain.usecase

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.UserList
import com.example.tokitoki.domain.repository.UserRepository
import javax.inject.Inject

interface GetTagSubscribersUseCase {
    suspend operator fun invoke(
        tagId: String,
        cursor: String?,
        limit: Int
    ): ResultWrapper<UserList>
}

class GetTagSubscribersUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : GetTagSubscribersUseCase {
    override suspend operator fun invoke(
        tagId: String,
        cursor: String?,
        limit: Int
    ): ResultWrapper<UserList> {
        return userRepository.getTagSubscribers(tagId, cursor, limit)
    }
} 