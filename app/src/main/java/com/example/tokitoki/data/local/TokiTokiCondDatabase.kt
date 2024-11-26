package com.example.tokitoki.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CategoryEntity::class, TagEntity::class, MySelfSentenceEntity::class], version = 1, exportSchema = false)
abstract class TokiTokiCondDatabase : RoomDatabase() {

    abstract fun tagDao(): TagDao
    abstract fun categoryDao(): CategoryDao
    abstract fun myselfSentenceDao(): MySelfSentenceDao

    companion object {
        const val DATABASE_NAME = "tokitoki_cond_database.db"
    }
}