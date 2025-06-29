package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MyTag
import com.example.tokitoki.domain.repository.MyProfileRepository
import com.example.tokitoki.domain.model.TagType
import javax.inject.Inject

interface GetMyTagsByTagTypeUseCase {
    suspend operator fun invoke(tagType: TagType): Result<List<MyTag>>
}

class GetMyTagsByTagTypeUseCaseImpl @Inject constructor(
    private val myProfileRepository: MyProfileRepository
) : GetMyTagsByTagTypeUseCase {
    override suspend fun invoke(tagType: TagType): Result<List<MyTag>> {
        return try {
            val tags = myProfileRepository.getUserTagsByTagTypeId(tagType.ordinal)
            Result.success(tags)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 