package com.example.tokitoki.data.repository

import android.content.Context
import com.example.tokitoki.BuildConfig
import com.example.tokitoki.domain.repository.AppVersionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import javax.inject.Inject

class AppVersionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AppVersionRepository {
    override fun getCurrentDbVersion(): String {
        return BuildConfig.CONDITION_DB_VERSION_CODE
    }

    override suspend fun downloadDbFromServer(): String {
        try {
            // 1. 에셋 파일 열기
            val assetsFileName = "fake_tokitoki_cond_database.db"
            val inputStream = context.assets.open(assetsFileName)

            // 2. 외부 저장소 경로 설정
            val outputFileName = "remote_tokitoki_cond_database.db"
            val outputFile = File(context.getExternalFilesDir(null), outputFileName)

            // 3. 파일 복사
            inputStream.use { input ->
                outputFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            // 4. 복사된 파일 경로 반환
            return outputFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            throw IOException("Failed to copy database from assets: ${e.message}")
        }
    }
}