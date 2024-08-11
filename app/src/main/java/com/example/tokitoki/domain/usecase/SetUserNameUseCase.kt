package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.UserRepository
import javax.inject.Inject

class SetUserNameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String) = userRepository.saveUserNameFromPreferences(name)
}