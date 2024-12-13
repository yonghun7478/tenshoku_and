package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.Tag
import com.example.tokitoki.domain.repository.TagRepository
import javax.inject.Inject

interface GetTagByTagIdWithCategoryIdUseCase {
    suspend operator fun invoke(categoryId: Int, ids: List<Int>): List<Tag>
}

class GetTagByTagIdWithCategoryIdUseCaseImpl @Inject constructor(
    private val tagRepository: TagRepository
) : GetTagByTagIdWithCategoryIdUseCase {
    override suspend operator fun invoke(categoryId: Int, ids: List<Int>): List<Tag> {
        return tagRepository.getTags(categoryId, ids)
    }
}
