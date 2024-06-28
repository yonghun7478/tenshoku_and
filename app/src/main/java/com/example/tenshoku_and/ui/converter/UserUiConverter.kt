package com.example.tenshoku_and.ui.converter

import com.example.tenshoku_and.domain.model.User
import com.example.tenshoku_and.ui.model.UserUiModel

object UserUiConverter {
    fun domainUserToUser(user: User): UserUiModel {
        return UserUiModel(
            name = user.name,
            username = user.username,
            email = user.email,
            phone = user.phone,
            website = user.website,
        )
    }
}