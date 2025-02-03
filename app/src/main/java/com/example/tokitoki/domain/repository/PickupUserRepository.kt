package com.example.tokitoki.domain.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.PickupUser

interface PickupUserRepository {
    suspend fun fetchPickupUsers(): ResultWrapper<List<PickupUser>>
    suspend fun likePickupUser(pickupUserId: String): ResultWrapper<Unit>
    suspend fun dislikePickupUser(pickupUserId: String): ResultWrapper<Unit>
}