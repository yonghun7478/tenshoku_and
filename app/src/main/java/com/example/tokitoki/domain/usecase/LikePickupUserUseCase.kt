package com.example.tokitoki.domain.usecase

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.repository.PickupUserRepository
import javax.inject.Inject

interface LikePickupUserUseCase {
    suspend operator fun invoke(pickupUserId: String): ResultWrapper<Unit>
}

class LikePickupUserUseCaseImpl @Inject constructor(private val repository: PickupUserRepository) : LikePickupUserUseCase {
    override suspend fun invoke(pickupUserId: String): ResultWrapper<Unit> {
        return repository.likePickupUser(pickupUserId)
    }
}