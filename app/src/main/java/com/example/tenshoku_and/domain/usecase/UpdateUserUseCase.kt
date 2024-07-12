package com.example.tenshoku_and.domain.usecase

import com.example.tenshoku_and.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(id: Int, name: String) = userRepository.updateUserFromDb(id, name)
}