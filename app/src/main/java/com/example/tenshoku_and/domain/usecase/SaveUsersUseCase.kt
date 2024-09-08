package com.example.tenshoku_and.domain.usecase

import com.example.tenshoku_and.domain.model.User
import com.example.tenshoku_and.domain.repository.UserRepository
import javax.inject.Inject

class SaveUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(users: List<User>) = userRepository.saveUsersToDb(users)
}