package com.example.tokitoki.fake

import com.example.tokitoki.di.IsTestModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [IsTestModule::class]  // 실제 모듈을 대체
)
object FakeIsTestModule {
    @Provides
    @Singleton
    fun provideIsTest(): Boolean {
        return true  // 테스트 환경에서는 true
    }
}