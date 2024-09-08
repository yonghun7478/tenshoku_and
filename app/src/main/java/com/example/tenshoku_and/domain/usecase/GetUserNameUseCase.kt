package com.example.tenshoku_and.domain.usecase

import com.example.tenshoku_and.domain.repository.UserRepository
import javax.inject.Inject

class GetUserNameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.getUserNameFromPreferences()
}