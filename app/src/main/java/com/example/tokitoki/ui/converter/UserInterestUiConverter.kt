package com.example.tokitoki.ui.converter

import com.example.tokitoki.domain.model.UserInterest
import com.example.tokitoki.ui.model.UserInterestItem

object UserInterestUiConverter {
    fun domainToUi(userInterest: UserInterest): UserInterestItem {
        return UserInterestItem(
            id = userInterest.id,
            title = userInterest.title,
            url = userInterest.url
        )
    }
}