package com.example.tokitoki.di

import android.content.Context
import androidx.room.Room
import com.example.tokitoki.data.local.CategoryDao
import com.example.tokitoki.data.local.MySelfSentenceDao
import com.example.tokitoki.data.local.TokiTokiCondDatabase
import com.example.tokitoki.data.local.TagDao
import com.example.tokitoki.data.local.TokiTokiLocalDatabase
import com.example.tokitoki.data.local.EncryptionHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import java.nio.charset.StandardCharsets
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
    fun provideTokiTokiLocalDatabase(
        @ApplicationContext context: Context,
        encryptionHelper: EncryptionHelper
    ): TokiTokiLocalDatabase {
        System.loadLibrary("sqlcipher")
        val password = encryptionHelper.getEncryptedPassword()
        val factory = SupportOpenHelperFactory(password.toByteArray(StandardCharsets.UTF_8))

        return Room.databaseBuilder(
            context,
            TokiTokiLocalDatabase::class.java,
            TokiTokiLocalDatabase.DATABASE_NAME
        ).openHelperFactory(factory).build()
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