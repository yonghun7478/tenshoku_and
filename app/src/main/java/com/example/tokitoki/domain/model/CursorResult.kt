package com.example.tokitoki.domain.model

data class CursorResult<T>(
    val data: List<T>,
    val nextCursor: String? // 다음 페이지 요청 시 사용할 커서 (Base64 인코딩된 문자열)
)