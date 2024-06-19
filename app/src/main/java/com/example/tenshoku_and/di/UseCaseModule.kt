package com.example.tenshoku_and.di

import com.example.tenshoku_and.domain.repository.UserRepository
import com.example.tenshoku_and.domain.usecase.GetUserFromApiUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetUserUseCase(userRepository: UserRepository): GetUserFromApiUseCase {
        return GetUserFromApiUseCase(userRepository)
    }
}