package com.example.tokitoki.domain.converter

import com.example.tokitoki.data.model.PickupUserResponse
import com.example.tokitoki.domain.model.PickupUser

object PickupUserConverter {
    fun fromResponse(response: PickupUserResponse): PickupUser {
        return PickupUser(
            id = response.id,
            thumbnail = response.thumbnail,
            age = response.age,
            name = response.name,
            location = response.location
        )
    }
}