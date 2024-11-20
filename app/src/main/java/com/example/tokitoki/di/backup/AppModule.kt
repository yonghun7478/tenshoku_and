package com.example.tokitoki.di.backup

import android.content.Context
import androidx.room.Room
import com.example.tokitoki.data.local.backup.AppDatabase
import com.example.tokitoki.data.local.backup.AppLocalDatabase
import com.example.tokitoki.data.local.EncryptionHelper
import com.example.tokitoki.data.local.backup.LocalUserDao
import com.example.tokitoki.data.local.backup.UserDao
import com.example.tokitoki.data.local.backup.UserPreferences
import com.example.tokitoki.data.local.backup.UserPreferencesImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import java.nio.charset.StandardCharsets
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
//    @Provides
//    @Singleton
//    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

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
    fun provideAppLocalDatabase(
        @ApplicationContext context: Context,
        encryptionHelper: EncryptionHelper
    ): AppLocalDatabase {
        System.loadLibrary("sqlcipher")
        val password = encryptionHelper.getEncryptedPassword()
        val factory = SupportOpenHelperFactory(password.toByteArray(StandardCharsets.UTF_8))

        return Room.databaseBuilder(
            context,
            AppLocalDatabase::class.java,
            AppLocalDatabase.DATABASE_NAME
        ).openHelperFactory(factory).build()
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