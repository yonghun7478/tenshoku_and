package com.example.tokitoki.data.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.data.dummy.DummyData
import com.example.tokitoki.domain.model.PickupUser
import com.example.tokitoki.domain.repository.PickupUserRepository
import javax.inject.Inject

class PickupUserRepositoryImpl @Inject constructor() : PickupUserRepository {
    override suspend fun fetchPickupUsers(): ResultWrapper<List<PickupUser>> {
        val allUsers = DummyData.getUsers()
        val pickupUserDetails = allUsers.shuffled().take(6)

        val pickupUsers = pickupUserDetails.map { userDetail ->
            PickupUser(
                id = userDetail.id,
                thumbnail = userDetail.thumbnailUrl,
                age = userDetail.age,
                name = userDetail.name,
                location = userDetail.location
            )
        }
        return ResultWrapper.Success(pickupUsers)
    }

    override suspend fun likePickupUser(pickupUserId: String): ResultWrapper<Unit> {
        return ResultWrapper.Success(Unit)
    }

    override suspend fun dislikePickupUser(pickupUserId: String): ResultWrapper<Unit> {
        return ResultWrapper.Success(Unit)
    }
}