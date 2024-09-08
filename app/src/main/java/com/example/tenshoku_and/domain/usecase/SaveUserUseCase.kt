package com.example.tenshoku_and.domain.usecase

import com.example.tenshoku_and.domain.model.User
import com.example.tenshoku_and.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User) = userRepository.saveUserToDb(user)
}