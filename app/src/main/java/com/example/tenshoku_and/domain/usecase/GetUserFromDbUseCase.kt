package com.example.tenshoku_and.domain.usecase

import com.example.tenshoku_and.domain.model.User
import com.example.tenshoku_and.domain.repository.UserRepository
import com.example.tenshoku_and.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserFromDbUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<User>>> {
        return userRepository.getUsersFromDb()
    }
}