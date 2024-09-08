package com.example.tokitoki.domain.usecase.backup

import com.example.tokitoki.domain.model.User
import com.example.tokitoki.domain.repository.UserRepository
import com.example.tokitoki.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserFromDbUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<User>>> {
        return userRepository.getUsersFromDb()
    }
}