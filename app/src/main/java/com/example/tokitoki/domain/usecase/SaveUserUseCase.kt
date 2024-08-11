package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.User
import com.example.tokitoki.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User) = userRepository.saveUserToDb(user)
}