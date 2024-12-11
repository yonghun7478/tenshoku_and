package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.Tag
import com.example.tokitoki.domain.repository.TagRepository
import javax.inject.Inject

interface GetTagByCategoryIdUseCase {
    suspend operator fun invoke(categoryId: Int): List<Tag>
}

class GetTagByCategoryIdUseCaseImpl @Inject constructor(
    private val tagRepository: TagRepository
) : GetTagByCategoryIdUseCase {
    override suspend operator fun invoke(categoryId: Int): List<Tag> {
        return tagRepository.getTags(categoryId)
    }
}
