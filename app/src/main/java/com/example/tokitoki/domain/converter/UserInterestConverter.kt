package com.example.tokitoki.domain.converter

import com.example.tokitoki.data.local.UserInterestWithCategoryEntity
import com.example.tokitoki.domain.model.UserInterest

object UserInterestConverter {
    // UserInterestWithCategory(Data Layer) -> UserInterest(Domain Layer)
    fun dataToDomain(userInterestWithCategoryEntity: UserInterestWithCategoryEntity): UserInterest {
        return UserInterest(
            id = userInterestWithCategoryEntity.interestId,
            title = userInterestWithCategoryEntity.interestTitle,
            url = userInterestWithCategoryEntity.interestUrl,
            categoryId = userInterestWithCategoryEntity.categoryId,
            categoryTitle = userInterestWithCategoryEntity.categoryTitle
        )
    }

    // UserInterest(Domain Layer) -> UserInterestWithCategory(Data Layer)
    fun domainToData(userInterest: UserInterest): UserInterestWithCategoryEntity {
        return UserInterestWithCategoryEntity(
            interestId = userInterest.id,
            interestTitle = userInterest.title,
            interestUrl = userInterest.url,
            categoryId = userInterest.categoryId,
            categoryTitle = userInterest.categoryTitle
        )
    }
}