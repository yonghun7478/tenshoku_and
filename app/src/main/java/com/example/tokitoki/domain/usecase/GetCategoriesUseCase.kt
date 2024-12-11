package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.Category
import com.example.tokitoki.domain.repository.TagRepository
import javax.inject.Inject

interface GetCategoriesUseCase {
    suspend operator fun invoke(): List<Category>
}

class GetCategoriesUseCaseImpl @Inject constructor(
    private val tagRepository: TagRepository
) : GetCategoriesUseCase {
    override suspend operator fun invoke(): List<Category> {
        return tagRepository.getCategories()
    }
}