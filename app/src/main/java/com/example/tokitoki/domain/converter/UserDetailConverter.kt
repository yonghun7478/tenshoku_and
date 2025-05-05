package com.example.tokitoki.domain.converter

import com.example.tokitoki.data.local.UserDetailEntity
import com.example.tokitoki.domain.model.UserDetail

object UserDetailConverter {
    fun entityToDomain(entity: UserDetailEntity): UserDetail =
        UserDetail(
            id = entity.id,
            name = entity.name,
            birthDay = entity.birthDay,
            isMale = entity.isMale,
            mySelfSentenceId = entity.mySelfSentenceId,
            email = entity.email,
            thumbnailUrl = entity.thumbnailUrl
        )

    fun domainToEntity(domain: UserDetail): UserDetailEntity =
        UserDetailEntity(
            id = domain.id,
            name = domain.name,
            birthDay = domain.birthDay,
            isMale = domain.isMale,
            mySelfSentenceId = domain.mySelfSentenceId,
            email = domain.email,
            thumbnailUrl = domain.thumbnailUrl
        )
} 