package com.example.tokitoki.data.model

data class AshiatoApiResponse(
    // JSON key "status"에 해당
    val status: String,

    // JSON key "message"에 해당 (Nullable)
    val message: String?,

    // JSON key "data"에 해당
    val data: AshiatoData // 내부 데이터 객체
)

/**
 * API 응답의 "data" 필드 구조
 */
data class AshiatoData(
    // JSON key "dailyProfileViews"에 해당
    val dailyAshiatoList: List<DailyAshiato> // 일별 아시아토 목록
)

/**
 * 일별 아시아토 기록 객체 ("dailyProfileViews" 배열의 요소)
 */
data class DailyAshiato(
    // JSON key "date"에 해당
    val date: String, // "YYYY-MM-DD"

    // JSON key "viewers"에 해당
    val viewers: List<AshiatoViewer> // 해당 날짜의 방문자(아시아토) 목록
)

/**
 * 개별 방문자(아시아토) 정보 객체 ("viewers" 배열의 요소)
 */
data class AshiatoViewer(
    // JSON key "userId"에 해당
    val userId: String,

    // JSON key "thumbnailUrl"에 해당
    val thumbnailUrl: String,

    // JSON key "age"에 해당
    val age: Int,

    // JSON key "region"에 해당
    val region: String,

    // JSON key "viewedTime"에 해당
    val viewedTime: String // "HH:mm"
)
