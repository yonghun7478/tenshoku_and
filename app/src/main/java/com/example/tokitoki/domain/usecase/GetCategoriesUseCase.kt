package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.Category
import com.example.tokitoki.domain.repository.TagRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {
    suspend operator fun invoke(): List<Category> {
        return tagRepository.getCategories()
    }
}
