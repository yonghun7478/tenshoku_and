package com.example.tokitoki.di

import com.example.tokitoki.domain.repository.UserInterestRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class TokiTokiRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUserInterestRepository(
        userRepositoryImpl: UserInterestRepository
    ): UserInterestRepository
}