package com.example.tenshoku_and.domain.usecase

import com.example.tenshoku_and.domain.model.User
import com.example.tenshoku_and.domain.repository.UserRepository
import com.example.tenshoku_and.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserFromApiUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<Result<List<User>>> {
        return userRepository.getUsersFromApi()
    }
}