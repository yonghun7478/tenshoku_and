package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.Category
import com.example.tokitoki.domain.repository.UserInterestRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val userInterestRepository: UserInterestRepository
) {
    suspend operator fun invoke(): List<Category> {
        return userInterestRepository.getCategories()
    }
}
