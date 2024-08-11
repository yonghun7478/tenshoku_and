package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.User
import com.example.tokitoki.domain.repository.UserRepository
import javax.inject.Inject

class SaveUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(users: List<User>) = userRepository.saveUsersToDb(users)
}