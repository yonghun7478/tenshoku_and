package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(id: Int, name: String) = userRepository.updateUserFromDb(id, name)
}