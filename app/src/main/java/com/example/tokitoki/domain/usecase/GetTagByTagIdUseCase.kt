package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.Tag
import com.example.tokitoki.domain.repository.TagRepository
import javax.inject.Inject

interface GetTagByTagIdUseCase {
    suspend operator fun invoke(ids: List<Int>): List<Tag>
}

class GetTagByTagIdUseCaseImpl @Inject constructor(
    private val tagRepository: TagRepository
) : GetTagByTagIdUseCase {
    override suspend operator fun invoke(ids: List<Int>): List<Tag> {
        return tagRepository.getTags(ids)
    }
}
