package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MyTag
import com.example.tokitoki.domain.repository.MyProfileRepository

import javax.inject.Inject

class GetMyTagUseCase @Inject constructor(
    private val myProfileRepository: MyProfileRepository
) {
    suspend operator fun invoke(): List<MyTag> {
        return listOf(MyTag(tagId = 1), MyTag(tagId = 2), MyTag(tagId = 3))
    }
}
