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
            PickupUserResponse("1", "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg", 1),
            PickupUserResponse("2", "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg", 2),
            PickupUserResponse("3", "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg", 3),
            PickupUserResponse("4", "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg", 4),
            PickupUserResponse("5", "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg", 5),
            PickupUserResponse("6", "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg", 6),
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