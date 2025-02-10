package com.example.tokitoki.domain.usecase

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.repository.PickupUserRepository
import javax.inject.Inject

interface DislikePickupUserUseCase {
    suspend operator fun invoke(pickupUserId: String): ResultWrapper<Unit>
}

class DislikePickupUserUseCaseImpl @Inject constructor(private val repository: PickupUserRepository) : DislikePickupUserUseCase {
    override suspend fun invoke(pickupUserId: String): ResultWrapper<Unit> {
        return repository.dislikePickupUser(pickupUserId)
    }
}