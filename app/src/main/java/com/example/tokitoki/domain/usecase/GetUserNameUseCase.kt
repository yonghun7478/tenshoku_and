package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.UserRepository
import javax.inject.Inject

class GetUserNameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.getUserNameFromPreferences()
}