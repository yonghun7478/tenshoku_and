package com.example.tokitoki.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IsTestModule {

    @Provides
    @Singleton
    fun provideIsTest(): Boolean {
        return false  // 실제 환경에서는 false
    }
}