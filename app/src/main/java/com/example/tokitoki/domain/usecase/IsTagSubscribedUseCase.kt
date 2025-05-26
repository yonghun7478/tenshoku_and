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
        // TODO: 실제 구현에서는 DAO를 사용하여 구독 여부를 확인해야 함
        // 현재는 더미 데이터로 구현
        return myProfileRepository.isTagSubscribed(tagId)
    }
} 