package com.example.tokitoki.data.repository

import android.content.Context
import com.example.tokitoki.BuildConfig
import com.example.tokitoki.data.local.CategoryDao
import com.example.tokitoki.data.local.DbVersionPreferences
import com.example.tokitoki.data.local.MySelfSentenceDao
import com.example.tokitoki.data.local.TagDao
import com.example.tokitoki.domain.repository.DbRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import javax.inject.Inject

class DbRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tagDao: TagDao,
    private val myselfSentenceDao: MySelfSentenceDao,
    private val categoryDao: CategoryDao,
    private val dbVersionPreferences: DbVersionPreferences
) : DbRepository {
    override fun getAssetDbVersion(): String {
        return BuildConfig.CONDITION_DB_VERSION_CODE
    }

    override fun getCurrentDbVersion(): String {
        return dbVersionPreferences.getDbVersion() ?: "0.0.0"
    }

    override fun setCurrentDbVersion(version: String) {
        dbVersionPreferences.saveDbVersion(version)
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

    override suspend fun isDatabaseEmpty(): Boolean {
        return tagDao.getRowCount() == 0 &&
                myselfSentenceDao.getRowCount() == 0 &&
                categoryDao.getRowCount() == 0
    }
}