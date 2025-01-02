package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MySelfSentence
import com.example.tokitoki.domain.repository.MySelfSentenceRepository
import javax.inject.Inject

interface GetMySelfSentenceUseCase {
    suspend operator fun invoke(id: Int): MySelfSentence
}

class GetMySelfSentenceUseCaseImpl @Inject constructor(
    private val myselfSentenceRepository: MySelfSentenceRepository
) : GetMySelfSentenceUseCase {
    override suspend operator fun invoke(id: Int): MySelfSentence {
        return myselfSentenceRepository.getSentence(id)
    }
}