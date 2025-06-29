package com.example.tokitoki.domain.usecase.tag

import com.example.tokitoki.domain.repository.MyProfileRepository
import javax.inject.Inject

interface RemoveUserTagUseCase {
    suspend operator fun invoke(tagId: Int)
}

class RemoveUserTagUseCaseImpl @Inject constructor(
    private val myProfileRepository: MyProfileRepository
) : RemoveUserTagUseCase {
    override suspend fun invoke(tagId: Int) {
        myProfileRepository.removeUserTag(tagId)
    }
} 