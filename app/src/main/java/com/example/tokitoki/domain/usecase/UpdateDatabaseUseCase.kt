package com.example.tokitoki.domain.usecase

import com.example.tokitoki.data.utils.DatabaseManager
import com.example.tokitoki.domain.repository.DbRepository
import javax.inject.Inject

interface UpdateDatabaseUseCase {
    suspend operator fun invoke(): Boolean
}

class UpdateDatabaseUseCaseImpl @Inject constructor(
    private val dbRepository: DbRepository,
    private val databaseManager: DatabaseManager,
) : UpdateDatabaseUseCase {
    override suspend operator fun invoke(): Boolean {
        return try {
            val currentDbVersion = dbRepository.getCurrentDbVersion()
            val assetDbVersion = dbRepository.getAssetDbVersion()
            val latestDbVersion = getLatestDbVersion()

            if (isVersionNewer(latestDbVersion, currentDbVersion)) {
                dbRepository.setCurrentDbVersion(latestDbVersion)
                if (latestDbVersion == assetDbVersion) {
                    databaseManager.replaceDatabaseWithAssets()
                } else {
                    val serverDbPath = dbRepository.downloadDbFromServer()
                    databaseManager.replaceDatabase(serverDbPath)
                }
            }
            true // 업데이트 성공
        } catch (e: Exception) {
            dbRepository.setCurrentDbVersion("0.0.0")
            e.printStackTrace()
            false // 업데이트 실패
        }
    }

    /**
     * 서버에 정의된 DB 버전을 가져옵니다.
     */
    private fun getLatestDbVersion(): String {
        return "2.0.0"
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