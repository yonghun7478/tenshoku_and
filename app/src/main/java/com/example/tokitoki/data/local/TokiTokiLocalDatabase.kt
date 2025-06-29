package com.example.tokitoki.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MyProfileEntity::class, MyTagEntity::class], version = 1, exportSchema = false)
abstract class TokiTokiLocalDatabase : RoomDatabase() {

    abstract fun myProfileDao(): MyProfileDao
    abstract fun myTagDao(): MyTagDao

    companion object {
        const val DATABASE_NAME = "tokitoki_local_database"
    }
}