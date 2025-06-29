package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.MyProfileRepository
import javax.inject.Inject

interface ClearMyTagUseCase {
    suspend operator fun invoke()
}

class ClearMyTagUseCaseImpl @Inject constructor(
    private val repository: MyProfileRepository
) : ClearMyTagUseCase {
    override suspend operator fun invoke() {
        repository.clearUserTags()
    }
}
