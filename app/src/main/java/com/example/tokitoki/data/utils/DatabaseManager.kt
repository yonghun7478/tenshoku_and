package com.example.tokitoki.data.utils

import android.content.Context
import androidx.room.Room
import com.example.tokitoki.data.local.CategoryDao
import com.example.tokitoki.data.local.MySelfSentenceDao
import com.example.tokitoki.data.local.TagDao
import com.example.tokitoki.data.local.TokiTokiCondDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import javax.inject.Inject

class DatabaseManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var tokiTokiCondDatabase: TokiTokiCondDatabase? = null
    private val databaseName = "tokitoki_cond_database.db" // 데이터베이스 이름
    private var curDbPath = ""

    /**
     * Replace the database with a file from the given path.
     * @param newDbPath The path to the new database file.
     */
    suspend fun replaceDatabaseFile(newDbPath: String) {
        val dbPath = context.getDatabasePath(databaseName)
        val newDbFile = File(newDbPath)

        try {
            if (dbPath.exists()) dbPath.delete()
            newDbFile.copyTo(dbPath, overwrite = true)
            curDbPath = newDbPath
        } catch (e: Exception) {
            e.printStackTrace()
            throw IOException("Failed to replace database: ${e.message}")
        }
    }

    /**
     * Replace the database with the file from the assets folder.
     */
    suspend fun replaceDatabaseFileWithAssets() {
        val dbPath = context.getDatabasePath(databaseName)
        val assetsFileName = "tokitoki_cond_database.db" // 에셋 폴더 최상단에 위치

        try {
            val assetsDbStream = context.assets.open(assetsFileName)

            if (dbPath.exists()) dbPath.delete()
            assetsDbStream.use { input ->
                dbPath.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            curDbPath = dbPath.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            throw IOException("Failed to replace database with assets: ${e.message}")
        }
    }

    fun initializeDatabase(customPath: String) {
        // 커스텀 경로에 파일이 없으면 예외 처리
        val databaseFile = File(customPath)
        if (!databaseFile.exists()) {
            throw IllegalArgumentException("Database file does not exist at path: $customPath")
        }

        // Room 데이터베이스 생성
        tokiTokiCondDatabase = Room.databaseBuilder(
            context,
            TokiTokiCondDatabase::class.java,
            databaseFile.name // 데이터베이스 이름은 파일명으로 지정
        ).createFromFile(databaseFile) // 커스텀 경로 지정
            .build()
    }

    fun getDatabase(): TokiTokiCondDatabase {
        return tokiTokiCondDatabase
            ?: throw IllegalStateException("Database is not initialized. Call initializeDatabase first.")
    }

    fun getTagDao(): TagDao = getDatabase().tagDao()
    fun getCategoryDao(): CategoryDao = getDatabase().categoryDao()
    fun getMySelfSentenceDao(): MySelfSentenceDao = getDatabase().myselfSentenceDao()
}