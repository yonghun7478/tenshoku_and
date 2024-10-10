package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.UserInterest
import com.example.tokitoki.domain.repository.UserInterestRepository
import javax.inject.Inject

class GetUserInterestsUseCase @Inject constructor(
    private val userInterestRepository: UserInterestRepository
) {
    suspend operator fun invoke(categoryId: Int): List<UserInterest> {
        return userInterestRepository.getUserInterests(categoryId)
    }
}
