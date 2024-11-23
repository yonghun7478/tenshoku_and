package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.Tag
import com.example.tokitoki.domain.repository.TagRepository
import javax.inject.Inject

class GetTagByTagIdUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {
    suspend operator fun invoke(ids:List<Int>): List<Tag> {
        return tagRepository.getTags(ids)
    }
}
