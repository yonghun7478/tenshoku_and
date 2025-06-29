package com.example.tokitoki.domain.model

sealed class MessageStatus {
    object Online : MessageStatus()
    object Offline : MessageStatus()
    data class LastSeen(val lastSeenTime: Long) : MessageStatus()
} 