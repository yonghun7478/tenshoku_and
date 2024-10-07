package com.example.tokitoki.domain.converter

import com.example.tokitoki.data.local.UserInterestWithCategory
import com.example.tokitoki.domain.model.UserInterest

object UserInterestConverter {
    // UserInterestWithCategory(Data Layer) -> UserInterest(Domain Layer)
    fun dataToDomain(userInterestWithCategory: UserInterestWithCategory): UserInterest {
        return UserInterest(
            id = userInterestWithCategory.interestId,
            title = userInterestWithCategory.interestTitle,
            url = userInterestWithCategory.interestUrl,
            categoryId = userInterestWithCategory.categoryId,
            categoryTitle = userInterestWithCategory.categoryTitle
        )
    }

    // UserInterest(Domain Layer) -> UserInterestWithCategory(Data Layer)
    fun domainToData(userInterest: UserInterest): UserInterestWithCategory {
        return UserInterestWithCategory(
            interestId = userInterest.id,
            interestTitle = userInterest.title,
            interestUrl = userInterest.url,
            categoryId = userInterest.categoryId,
            categoryTitle = userInterest.categoryTitle
        )
    }
}