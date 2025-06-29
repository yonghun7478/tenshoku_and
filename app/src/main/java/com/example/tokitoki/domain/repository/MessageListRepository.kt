package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.CursorResult
import com.example.tokitoki.domain.model.MatchingUser
import com.example.tokitoki.domain.model.PreviousChat

interface MessageListRepository {
    /**
     * 매칭된 유저 목록을 가져옵니다. (페이징)
     * @param cursor 다음 페이지를 위한 커서 (Base64 인코딩), null이면 첫 페이지
     * @param limit 페이지 당 아이템 수
     * @return 매칭 유저 목록과 다음 커서를 포함한 결과
     */
    suspend fun getMatchingUsers(
        cursor: String?,
        limit: Int = 10
    ): Result<CursorResult<MatchingUser>>

    /**
     * 이전 대화 목록을 가져옵니다. (페이징)
     * @param cursor 다음 페이지를 위한 커서 (Base64 인코딩된 "messageId:timestamp"), null이면 첫 페이지
     * @param limit 페이지 당 아이템 수
     * @return 이전 대화 목록과 다음 커서를 포함한 결과
     */
    suspend fun getPreviousChats(
        cursor: String?,
        limit: Int = 20
    ): Result<CursorResult<PreviousChat>>

    /**
     * 현재 대화를 이전 대화 목록으로 이동시킵니다.
     * @param userId 이동시킬 대화의 사용자 ID
     * @return 작업 성공 여부
     */
    suspend fun moveMessageToPrevious(userId: String): Result<Unit>
}