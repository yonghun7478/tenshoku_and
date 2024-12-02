package com.example.tokitoki.domain.converter

import com.example.tokitoki.data.local.MyProfileEntity
import com.example.tokitoki.domain.model.MyProfile

object MyProfileConverter {

    // MyProfileEntity -> MyProfile
    fun entityToDomain(entity: MyProfileEntity): MyProfile {
        return MyProfile(
            id = entity.id,
            name = entity.name,
            age = entity.age, // String -> Int 변환, 기본값 0
            isMale = entity.isMale,
            thumbnailImageUri = entity.thumbnailImageUri,
            mySelfSentence = entity.mySelfSentence
        )
    }

    // MyProfile -> MyProfileEntity
    fun domainToEntity(domain: MyProfile): MyProfileEntity {
        return MyProfileEntity(
            id = domain.id,
            name = domain.name,
            age = domain.age, // Int -> String 변환
            isMale = domain.isMale,
            thumbnailImageUri = domain.thumbnailImageUri,
            mySelfSentence = domain.mySelfSentence
        )
    }
}