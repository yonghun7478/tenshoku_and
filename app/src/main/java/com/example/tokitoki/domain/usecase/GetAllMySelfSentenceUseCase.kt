package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MySelfSentence
import com.example.tokitoki.domain.repository.MySelfSentenceRepository
import javax.inject.Inject

interface GetAllMySelfSentenceUseCase {
    suspend operator fun invoke(): List<MySelfSentence>
}

class GetAllMySelfSentenceUseCaseImpl @Inject constructor(
    private val myselfSentenceRepository: MySelfSentenceRepository
) : GetAllMySelfSentenceUseCase {
    override suspend operator fun invoke(): List<MySelfSentence> {
        return myselfSentenceRepository.getAllSentences()
    }
}