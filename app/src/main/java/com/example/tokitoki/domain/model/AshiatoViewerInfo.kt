package com.example.tokitoki.domain.model

import com.example.tokitoki.data.model.AshiatoData
import com.example.tokitoki.data.model.AshiatoViewer
import com.example.tokitoki.data.model.DailyAshiato

/**
 * 아시아토(방문자) 정보 (Domain Model/Entity)
 */
data class AshiatoViewerInfo(
    val id: String, // 유저 고유 ID
    val thumbnailUrl: String,
    val age: Int,
    val region: String,
    val viewedTime: String // "HH:mm"
)

/**
 * 특정 날짜의 아시아토(방문) 기록 (Domain Model/Entity)
 */
data class DailyAshiatoLog(
    val date: String, // "YYYY-MM-DD"
    val viewers: List<AshiatoViewerInfo> // Domain 모델 리스트
)

/**
 * 아시아토 기록 전체 목록 (시간순)을 나타내는 Domain Model/Entity.
 * Data Layer의 AshiatoData에 대응될 수 있습니다.
 */
data class AshiatoTimeline(
    val dailyLogs: List<DailyAshiatoLog>
)


/**
 * Data 레이어의 AshiatoViewer 모델을 Domain 레이어의 AshiatoViewerInfo 모델로 변환합니다.
 */
fun AshiatoViewer.toDomain(): AshiatoViewerInfo {
    return AshiatoViewerInfo(
        id = this.userId,
        thumbnailUrl = this.thumbnailUrl,
        age = this.age,
        region = this.region,
        viewedTime = this.viewedTime
    )
}

/**
 * Data 레이어의 DailyAshiato 모델을 Domain 레이어의 DailyAshiatoLog 모델로 변환합니다.
 * 내부의 AshiatoViewer 리스트도 함께 변환합니다.
 */
fun DailyAshiato.toDomain(): DailyAshiatoLog {
    return DailyAshiatoLog(
        date = this.date,
        viewers = this.viewers.map { viewerData -> viewerData.toDomain() } // 리스트 매핑
    )
}

/**
 * Data 레이어의 AshiatoData 모델 (API 응답의 data 부분 또는 전체 데이터)을
 * Domain 레이어의 AshiatoTimeline 모델로 변환합니다.
 * Repository 구현체 등에서 사용될 수 있습니다.
 */
fun AshiatoData.toDomain(): AshiatoTimeline { // 반환 타입을 AshiatoTimeline으로 변경
    // 매핑된 리스트를 AshiatoTimeline 객체로 감싸서 반환
    return AshiatoTimeline(
        dailyLogs = this.dailyAshiatoList.map { dailyAshiatoData -> dailyAshiatoData.toDomain() }
    )
}