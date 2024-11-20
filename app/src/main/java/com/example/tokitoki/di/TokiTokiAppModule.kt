package com.example.tokitoki.di

import android.content.Context
import androidx.room.Room
import com.example.tokitoki.data.local.CategoryDao
import com.example.tokitoki.data.local.MySelfSentenceDao
import com.example.tokitoki.data.local.TokiTokiCondDatabase
import com.example.tokitoki.data.local.TagDao
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
object TokiTokiAppModule {
    @Provides
    @Singleton
    fun provideTokiTokiCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideTokiTokiCondDatabase(@ApplicationContext context: Context): TokiTokiCondDatabase {
        return Room.databaseBuilder(
            context,
            TokiTokiCondDatabase::class.java,
            TokiTokiCondDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTagDao(tokiTokiCondDatabase: TokiTokiCondDatabase): TagDao {
        return tokiTokiCondDatabase.tagDao()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(tokiTokiCondDatabase: TokiTokiCondDatabase): CategoryDao {
        return tokiTokiCondDatabase.categoryDao()
    }

    @Provides
    @Singleton
    fun provideMySelfSentenceDao(tokiTokiCondDatabase: TokiTokiCondDatabase): MySelfSentenceDao {
        return tokiTokiCondDatabase.myselfSentenceDao()
    }
}