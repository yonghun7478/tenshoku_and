package com.example.tokitoki.ui.converter

import com.example.tokitoki.domain.model.User
import com.example.tokitoki.ui.model.UserUiModel

object UserUiMapper {
    fun domainToUi(user: User): UserUiModel {
        return UserUiModel(
            thumbnailUrl = user.thumbnailUrl,
            age = user.age
        )
    }
}