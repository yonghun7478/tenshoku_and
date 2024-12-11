package com.example.tokitoki.fake.di

import com.example.tokitoki.data.repository.MySelfSentenceRepositoryImpl
import com.example.tokitoki.di.TokiTokiRepositoryModule
import com.example.tokitoki.domain.repository.MySelfSentenceRepository
import com.example.tokitoki.domain.repository.TagRepository
import com.example.tokitoki.fake.repo.FakeTagRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [TokiTokiRepositoryModule::class]  // 실제 모듈을 대체
)
abstract class FakeRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindFakeTagRepository(
        fakeTagRepositoryImpl: FakeTagRepositoryImpl
    ): TagRepository

    @Binds
    @Singleton
    abstract fun bindFakeMySelfSentenceRepository(
        mySelfSentenceRepositoryImpl: MySelfSentenceRepositoryImpl
    ): MySelfSentenceRepository
}