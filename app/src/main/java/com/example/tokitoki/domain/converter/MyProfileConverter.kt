package com.example.tokitoki.domain.converter

import com.example.tokitoki.data.local.MyProfileEntity
import com.example.tokitoki.domain.model.MyProfile

object MyProfileConverter {

    // MyProfileEntity -> MyProfile
    fun entityToDomain(entity: MyProfileEntity): MyProfile {
        return MyProfile(
            id = entity.id,
            name = entity.name,
            birthDay = entity.birthDay, // String -> Int 변환, 기본값 0
            isMale = entity.isMale,
            mySelfSentenceId = entity.mySelfSentenceId,
            email = entity.email,
            thumbnailUrl = entity.thumbnailUrl
        )
    }

    // MyProfile -> MyProfileEntity
    fun domainToEntity(domain: MyProfile): MyProfileEntity {
        return MyProfileEntity(
            id = domain.id,
            name = domain.name,
            birthDay = domain.birthDay, // Int -> String 변환
            isMale = domain.isMale,
            mySelfSentenceId = domain.mySelfSentenceId,
            email = domain.email,
            thumbnailUrl = domain.thumbnailUrl
        )
    }
}