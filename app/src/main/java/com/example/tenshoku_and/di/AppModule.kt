package com.example.tenshoku_and.di

import android.content.Context
import androidx.room.Room
import com.example.tenshoku_and.data.local.AppDatabase
import com.example.tenshoku_and.data.local.AppLocalDatabase
import com.example.tenshoku_and.data.local.LocalUserDao
import com.example.tenshoku_and.data.local.UserDao
import com.example.tenshoku_and.data.local.UserPreferences
import com.example.tenshoku_and.data.local.UserPreferencesImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideAppLocalDatabase(@ApplicationContext context: Context): AppLocalDatabase {
        return Room.databaseBuilder(
            context,
            AppLocalDatabase::class.java,
            AppLocalDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideLocalUserDao(appLocalDatabase: AppLocalDatabase): LocalUserDao {
        return appLocalDatabase.localUserDao()
    }

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferencesImpl(context)
    }
}