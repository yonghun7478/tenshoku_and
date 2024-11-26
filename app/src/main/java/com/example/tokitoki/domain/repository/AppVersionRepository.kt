package com.example.tokitoki.domain.repository

interface AppVersionRepository {
    fun getCurrentDbVersion(): String // Gradle 변수에서 가져오기
    suspend fun downloadDbFromServer(): String // 서버에서 가짜 DB 경로 반환
}