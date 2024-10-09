package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.Category
import com.example.tokitoki.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): List<Category> {
        return categoryRepository.getCategories()
    }
}
