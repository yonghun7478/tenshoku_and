package com.example.tokitoki.data.utils

import android.content.Context
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
        } catch (e: Exception) {
            e.printStackTrace()
            throw IOException("Failed to replace database with assets: ${e.message}")
        }
    }
}