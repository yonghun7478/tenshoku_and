package com.example.tokitoki.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CategoryEntity::class, UserInterestEntity::class], version = 1, exportSchema = false)
abstract class TokiTokiCondDatabase : RoomDatabase() {

    abstract fun userInterestDao(): UserInterestDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        const val DATABASE_NAME = "tokitoki_cond_database"
    }
}