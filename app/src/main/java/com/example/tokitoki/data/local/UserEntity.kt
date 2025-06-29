package com.example.tokitoki.data.local

data class UserEntity(
    val id: String,
    val thumbnailUrl: String,
    val age: Int,
    val createdAt: Long,
    val lastLoginAt: Long
)