package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.UserInterest
import javax.inject.Inject

class GetUserInterestUseCase @Inject constructor() {
    suspend operator fun invoke(categoryId: Int): List<UserInterest> {
        return emptyList()
    }
}