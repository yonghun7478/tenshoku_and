package com.example.tokitoki.data.model

import com.example.tokitoki.domain.model.PreviousChat
import java.time.Instant
import java.time.ZoneId
import java.time.LocalDate

data class PreviousChatDto(
    val messageId: String, // 커서에 사용될 ID
    val partnerNickname: String,
    val partnerHometown: String,
    val lastMessageTimestamp: Long, // LocalDate로 변환될 타임스탬프
    val partnerProfileImageUrl: String
)

// --- PreviousChat Mapper ---
fun PreviousChatDto.toDomain(): PreviousChat {
    // Long 타임스탬프를 LocalDate로 변환
    val date = Instant.ofEpochMilli(this.lastMessageTimestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    return PreviousChat(
        id = this.messageId,
        nickname = this.partnerNickname,
        hometown = this.partnerHometown,
        lastMessageDate = date,
        thumbnail = this.partnerProfileImageUrl
    )
}
