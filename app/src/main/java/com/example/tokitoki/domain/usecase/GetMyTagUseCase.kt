package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MyTag
import com.example.tokitoki.domain.repository.MyProfileRepository

import javax.inject.Inject

interface GetMyTagUseCase {
    suspend operator fun invoke(): List<MyTag>
}

class GetMyTagUseCaseImpl @Inject constructor(
    private val myProfileRepository: MyProfileRepository
) : GetMyTagUseCase {
    override suspend operator fun invoke(): List<MyTag> {
        return myProfileRepository.getUserTagsAsDomain()
    }
}
