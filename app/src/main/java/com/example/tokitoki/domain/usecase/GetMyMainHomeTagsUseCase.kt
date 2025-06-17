package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import com.example.tokitoki.domain.repository.MyProfileRepository
import com.example.tokitoki.domain.repository.TagRepository
import javax.inject.Inject

interface GetMyMainHomeTagsUseCase{
    suspend operator fun invoke(): Result<List<MainHomeTag>>
}
class GetMyMainHomeTagsUseCaseImpl @Inject constructor(
    private val mainHomeTagRepository: MainHomeTagRepository,
    private val myProfileRepository: MyProfileRepository,
    private val tagRepository: TagRepository
): GetMyMainHomeTagsUseCase{
    override suspend fun invoke(): Result<List<MainHomeTag>> {
        return try {
            val myTags = myProfileRepository.getUserTags()
            val myTagIds = myTags.map { it.tagId }

            val mainHomeTags = tagRepository.getTags(myTagIds)
                .map { it.copy(isSubscribed = true) }

            Result.success(mainHomeTags)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}