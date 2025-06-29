package com.example.tokitoki.domain.usecase

import android.util.Log
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

            Log.d("UpdateDatabaseUseCase", "Current DB Version: $currentDbVersion, Asset DB Version: $assetDbVersion, Latest DB Version: $latestDbVersion")

            if (isVersionNewer(latestDbVersion, currentDbVersion)) {
                Log.d("UpdateDatabaseUseCase", "Newer version detected: $latestDbVersion. Updating database.")
                dbRepository.setCurrentDbVersion(latestDbVersion)
                if (latestDbVersion == assetDbVersion) {
                    databaseManager.replaceDatabaseFileWithAssets()
                    Log.d("UpdateDatabaseUseCase", "Database replaced with asset version.")
                } else {
                    val serverDbPath = dbRepository.downloadDbFromServer()
                    databaseManager.replaceDatabaseFile(serverDbPath)
                    Log.d("UpdateDatabaseUseCase", "Database replaced with server version from: $serverDbPath")
                }
            } else {
                Log.d("UpdateDatabaseUseCase", "No newer version found. Database is up to date.")
            }

            // 데이터베이스 업데이트 성공 후, 데이터베이스가 실제로 열리고 App Inspection에 표시되도록 간단한 쿼리를 실행
            if (dbRepository.isDatabaseEmpty()) {
                Log.d("UpdateDatabaseUseCase", "Database appears empty, forcing Room initialization with a query.")
                // isDatabaseEmpty() 내부에서 이미 DAO 접근을 통해 데이터베이스가 열리므로 추가적인 명시적 쿼리 호출은 불필요
            }

            Log.d("UpdateDatabaseUseCase", "Database update successful.")
            true // 업데이트 성공
        } catch (e: Exception) {
            dbRepository.setCurrentDbVersion("0.0.0")
            Log.e("UpdateDatabaseUseCase", "Database update failed: ${e.message}", e)
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