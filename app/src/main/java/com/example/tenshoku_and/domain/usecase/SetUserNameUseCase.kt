package com.example.tenshoku_and.domain.usecase

import com.example.tenshoku_and.domain.repository.UserRepository
import javax.inject.Inject

class SetUserNameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String) = userRepository.saveUserNameFromPreferences(name)
}