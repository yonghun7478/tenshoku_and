package com.example.tokitoki.data.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.data.model.PickupUserResponse
import com.example.tokitoki.domain.converter.PickupUserConverter
import com.example.tokitoki.domain.model.PickupUser
import com.example.tokitoki.domain.repository.PickupUserRepository
import javax.inject.Inject

class PickupUserRepositoryImpl @Inject constructor() : PickupUserRepository {
    override suspend fun fetchPickupUsers(): ResultWrapper<List<PickupUser>> {
        val mockData = listOf(
            PickupUserResponse("1", "https://example.com/image1.jpg", 25),
            PickupUserResponse("2", "https://example.com/image2.jpg", 30)
        ).map { PickupUserConverter.fromResponse(it) }
        return ResultWrapper.Success(mockData)
    }

    override suspend fun likePickupUser(pickupUserId: String): ResultWrapper<Unit> {
        return ResultWrapper.Success(Unit)
    }

    override suspend fun dislikePickupUser(pickupUserId: String): ResultWrapper<Unit> {
        return ResultWrapper.Success(Unit)
    }
}