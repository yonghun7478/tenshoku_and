package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MySelfSentence
import com.example.tokitoki.domain.repository.MySelfSentenceRepository
import javax.inject.Inject

class GetMySelfSentenceUseCase @Inject constructor(
    private val myselfSentenceRepository: MySelfSentenceRepository
) {
    suspend operator fun invoke(): List<MySelfSentence> {
        return myselfSentenceRepository.getAllSentences()
    }
}