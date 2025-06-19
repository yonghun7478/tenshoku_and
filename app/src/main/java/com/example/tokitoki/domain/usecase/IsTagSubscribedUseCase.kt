package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.MyProfileRepository
import javax.inject.Inject

interface IsTagSubscribedUseCase {
    suspend operator fun invoke(tagId: String): Boolean
}

class IsTagSubscribedUseCaseImpl @Inject constructor(
    private val myProfileRepository: MyProfileRepository
) : IsTagSubscribedUseCase {
    override suspend operator fun invoke(tagId: String): Boolean {
        return myProfileRepository.isTagSubscribed(tagId)
    }
} 