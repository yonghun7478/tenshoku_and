package com.example.tokitoki.data.local.backup

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalUserEntity::class], version = 1, exportSchema = false)
abstract class AppLocalDatabase : RoomDatabase() {

    abstract fun localUserDao(): LocalUserDao

    companion object {
        const val DATABASE_NAME = "tokitoki_app_local_database"
    }
}