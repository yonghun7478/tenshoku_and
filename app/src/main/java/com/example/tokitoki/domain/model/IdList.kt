package com.example.tokitoki.domain.model

data class IdList(
    val ids: List<String>,
    val nextCursor: String?,
    val isLastPage: Boolean
) 