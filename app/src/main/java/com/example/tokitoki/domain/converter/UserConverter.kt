package com.example.tokitoki.domain.converter

import com.example.tokitoki.data.local.UserEntity
import com.example.tokitoki.domain.model.User

object UserConverter {
    fun dataToDomain(userEntity: UserEntity): User {
        return User(
            thumbnailUrl = userEntity.thumbnailUrl,
            age = userEntity.age
        )
    }
}