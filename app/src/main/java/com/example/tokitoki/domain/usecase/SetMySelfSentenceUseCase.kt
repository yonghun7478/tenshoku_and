package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.MyProfileRepository
import javax.inject.Inject

// UseCase 인터페이스
interface SetMySelfSentenceUseCase {
    suspend operator fun invoke(myselfSentence: String): String
}

// UseCase 구현 클래스
class SetMySelfSentenceUseCaseImpl @Inject constructor(
    private val repository: MyProfileRepository
) : SetMySelfSentenceUseCase {
    override suspend fun invoke(myselfSentence: String): String {
        repository.updateMySelfSentence(myselfSentence)
        val myProfile = repository.getUserProfile()
        return myProfile?.mySelfSentence ?: ""
    }
}
