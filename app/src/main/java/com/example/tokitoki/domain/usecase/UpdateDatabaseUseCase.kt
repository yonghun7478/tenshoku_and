package com.example.tokitoki.domain.usecase

import com.example.tokitoki.data.utils.DatabaseManager
import com.example.tokitoki.domain.repository.AppVersionRepository
import javax.inject.Inject

interface UpdateDatabaseUseCase {
    suspend operator fun invoke(): Boolean
}

class UpdateDatabaseUseCaseImpl @Inject constructor(
    private val repository: AppVersionRepository,
    private val databaseManager: DatabaseManager,
) : UpdateDatabaseUseCase {
    override suspend operator fun invoke(): Boolean {
        return try {
            // 1. 현재 앱의 데이터베이스 버전 확인
            val currentDbVersion = repository.getCurrentDbVersion()

            // 2. 서버에 선언된 DB 버전 가져오기
            val latestDbVersion = getLatestDbVersion()

            // 3. 버전 비교
            if (isVersionNewer(latestDbVersion, currentDbVersion)) {
                // 서버에서 새 데이터베이스 파일 다운로드
                val serverDbPath = repository.downloadDbFromServer()

                // 서버 DB로 교체
                databaseManager.replaceDatabase(serverDbPath)
            } else {
                // Gradle DB가 최신이므로 에셋 DB로 교체
                databaseManager.replaceDatabaseWithAssets()
            }

            true // 성공
        } catch (e: Exception) {
            databaseManager.replaceDatabaseWithAssets()
            e.printStackTrace()
            false // 실패
        }
    }

    /**
     * 서버에 정의된 DB 버전을 가져옵니다.
     */
    private fun getLatestDbVersion(): String {
        return  "2.0.0"
    }

    /**
     * 두 버전을 비교합니다.
     * @return true if `newVersion` is newer than `currentVersion`.
     */
    private fun isVersionNewer(newVersion: String, currentVersion: String): Boolean {
        val newParts = newVersion.split(".").map { it.toIntOrNull() ?: 0 }
        val currentParts = currentVersion.split(".").map { it.toIntOrNull() ?: 0 }

        for (i in newParts.indices) {
            val newPart = newParts.getOrElse(i) { 0 }
            val currentPart = currentParts.getOrElse(i) { 0 }

            if (newPart > currentPart) return true
            if (newPart < currentPart) return false
        }
        return false
    }
}