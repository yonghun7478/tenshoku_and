package com.example.tokitoki.domain.model

data class UserList(
    val users: List<User>,
    val nextCursor: String?, // 도메인 클래스에 커서 포함
    val isLastPage: Boolean
)

data class User(
    val thumbnailUrl: String,
    val age: Int
)