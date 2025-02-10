package com.example.tokitoki.ui.converter

import com.example.tokitoki.domain.model.PickupUser
import com.example.tokitoki.ui.model.PickupUserItem

object PickupUserMapper {
    fun toPresentation(pickupUser: PickupUser): PickupUserItem {
        return PickupUserItem(id = pickupUser.id, thumbnail = pickupUser.thumbnail, age = pickupUser.age)
    }
}