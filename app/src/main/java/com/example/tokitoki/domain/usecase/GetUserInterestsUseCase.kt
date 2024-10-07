package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.converter.UserInterestConverter
import com.example.tokitoki.domain.model.UserInterest
import com.example.tokitoki.domain.repository.UserInterestRepository

class GetUserInterestsUseCase(
    private val userInterestRepository: UserInterestRepository
) {
    suspend fun execute(categoryId: Int): List<UserInterest> {
        // 데이터 계층의 모델을 도메인 모델로 변환
        val userInterestEntities = userInterestRepository.getUserInterests(categoryId)
        return userInterestEntities.map { UserInterestConverter.dataToDomain(it) }
    }
}
