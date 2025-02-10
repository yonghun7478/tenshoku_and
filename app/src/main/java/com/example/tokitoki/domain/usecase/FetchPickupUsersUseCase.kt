package com.example.tokitoki.domain.usecase

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.PickupUser
import com.example.tokitoki.domain.repository.PickupUserRepository
import javax.inject.Inject

interface FetchPickupUsersUseCase {
    suspend operator fun invoke(): ResultWrapper<List<PickupUser>>
}

class FetchPickupUsersUseCaseImpl @Inject constructor(private val repository: PickupUserRepository) : FetchPickupUsersUseCase {
    override suspend fun invoke(): ResultWrapper<List<PickupUser>> {
        return repository.fetchPickupUsers()
    }
}