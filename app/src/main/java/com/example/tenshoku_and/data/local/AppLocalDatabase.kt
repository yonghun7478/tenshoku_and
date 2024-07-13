package com.example.tenshoku_and.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppLocalDatabase : RoomDatabase() {

    abstract fun localUserDao(): LocalUserDao

    companion object {
        const val DATABASE_NAME = "tenshoku_app_local_database"
    }
}