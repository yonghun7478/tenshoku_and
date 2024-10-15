package com.example.tokitoki.ui.model.backup

import com.example.tokitoki.domain.model.User

object UserUiConverter {
    fun domainUserToUser(user: User): UserUiModel {
        return UserUiModel(
            id = user.id,
            name = user.name,
            username = user.username,
            email = user.email,
            phone = user.phone,
            website = user.website,
        )
    }
}