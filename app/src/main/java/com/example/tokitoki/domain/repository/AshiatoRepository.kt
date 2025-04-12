package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.DailyAshiatoLog

/**
 * 페이징된 아시아토 목록과 다음 페이지 커서를 담는 데이터 클래스
 * Repository의 반환 타입으로 사용됩니다.
 */
data class AshiatoPage(
    val logs: List<DailyAshiatoLog>, // 현재 페이지의 아시아토 기록 리스트
    val nextCursor: String?         // 다음 페이지를 로드하기 위한 커서 (null이면 마지막 페이지)
)

/**
 * 아시아토(프로필 방문 기록) 데이터 관련 Repository 인터페이스
 */
interface AshiatoRepository {

    /**
     * 아시아토 목록의 특정 페이지를 가져옵니다.
     *
     * @param cursor 다음 페이지를 로드하기 위한 커서. 첫 페이지 로드 시에는 null 전달.
     * @param limit 한 번에 가져올 항목 수 (선택 사항, 서버와 협의 필요).
     * @return Result 객체. 성공 시 AshiatoPage(로그 목록 + 다음 커서) 포함, 실패 시 Exception 포함.
     */
    suspend fun getAshiatoPage(cursor: String?, limit: Int? = null): Result<AshiatoPage>

    // 필요하다면 다른 함수 추가 (예: 특정 유저의 방문 기록 삭제 등)
}
