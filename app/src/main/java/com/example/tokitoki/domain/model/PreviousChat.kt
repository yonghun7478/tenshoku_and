package com.example.tokitoki.domain.model

import java.time.LocalDate

data class PreviousChat(
    val id: String,
    val nickname: String,
    val hometown: String,
    val lastMessageDate: LocalDate,
    val thumbnail: String
)