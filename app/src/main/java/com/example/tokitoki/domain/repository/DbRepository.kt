package com.example.tokitoki.domain.repository

interface DbRepository {
    fun getAssetDbVersion(): String // Gradle 변수에서 가져오기
    fun getCurrentDbVersion(): String // preference 에서 가져오기
    fun setCurrentDbVersion(version: String)
    suspend fun downloadDbFromServer(): String // 서버에서 가짜 DB 경로 반환
    suspend fun isDatabaseEmpty(): Boolean
}