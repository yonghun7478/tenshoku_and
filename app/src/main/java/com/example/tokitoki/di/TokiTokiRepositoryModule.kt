package com.example.tokitoki.di

import com.example.tokitoki.data.repository.MyProfileRepositoryImpl
import com.example.tokitoki.data.repository.MySelfSentenceRepositoryImpl
import com.example.tokitoki.data.repository.TagRepositoryImpl
import com.example.tokitoki.domain.repository.MyProfileRepository
import com.example.tokitoki.domain.repository.MySelfSentenceRepository
import com.example.tokitoki.domain.repository.TagRepository
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
    abstract fun bindTagRepository(
        tagRepositoryImpl: TagRepositoryImpl
    ): TagRepository

    @Binds
    @Singleton
    abstract fun bindMySelfSentenceRepository(
        mySelfSentenceRepositoryImpl: MySelfSentenceRepositoryImpl
    ): MySelfSentenceRepository

    @Binds
    @Singleton
    abstract fun bindMyProfileRepository(
        myProfileRepositoryImpl: MyProfileRepositoryImpl
    ): MyProfileRepository
}