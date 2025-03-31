package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.CursorResult
import com.example.tokitoki.domain.model.PreviousChat
import com.example.tokitoki.domain.repository.MessageRepository
import javax.inject.Inject

/**
 * 이전 대화 목록을 가져오는 UseCase 인터페이스
 */
interface GetPreviousChatsUseCase {
    /**
     * UseCase를 실행합니다.
     * @param cursor 다음 페이지를 위한 커서 (Base64 인코딩된 "messageId:timestamp"), null이면 첫 페이지
     * @param limit 페이지 당 아이템 수 (기본값 20)
     * @return 이전 대화 목록과 다음 커서를 포함한 Result
     */
    suspend operator fun invoke(cursor: String?, limit: Int = 10): Result<CursorResult<PreviousChat>>
}

class GetPreviousChatsUseCaseImpl @Inject constructor(
    private val messageRepository: MessageRepository
) : GetPreviousChatsUseCase { // 인터페이스 상속

    /**
     * MessageRepository를 통해 이전 대화 데이터를 가져옵니다.
     */
    override suspend operator fun invoke(cursor: String?, limit: Int): Result<CursorResult<PreviousChat>> {
        // Repository의 함수를 호출하고 결과를 그대로 반환
        return messageRepository.getPreviousChats(cursor, limit)
        // 필요 시 여기에 추가적인 비즈니스 로직을 넣을 수 있습니다.
    }
}