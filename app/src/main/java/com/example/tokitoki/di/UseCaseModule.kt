package com.example.tokitoki.di

import com.example.tokitoki.domain.usecase.CalculateAgeUseCase
import com.example.tokitoki.domain.usecase.CalculateAgeUseCaseImpl
import com.example.tokitoki.domain.usecase.GetCategoriesUseCase
import com.example.tokitoki.domain.usecase.GetCategoriesUseCaseImpl
import com.example.tokitoki.domain.usecase.GetMyProfileUseCase
import com.example.tokitoki.domain.usecase.GetMyProfileUseCaseImpl
import com.example.tokitoki.domain.usecase.GetMySelfSentenceUseCase
import com.example.tokitoki.domain.usecase.GetMySelfSentenceUseCaseImpl
import com.example.tokitoki.domain.usecase.GetMyTagUseCase
import com.example.tokitoki.domain.usecase.GetMyTagUseCaseImpl
import com.example.tokitoki.domain.usecase.GetTagByCategoryIdUseCase
import com.example.tokitoki.domain.usecase.GetTagByCategoryIdUseCaseImpl
import com.example.tokitoki.domain.usecase.GetTagByTagIdUseCase
import com.example.tokitoki.domain.usecase.GetTagByTagIdUseCaseImpl
import com.example.tokitoki.domain.usecase.SetMyProfileUseCase
import com.example.tokitoki.domain.usecase.SetMyProfileUseCaseImpl
import com.example.tokitoki.domain.usecase.UpdateAgeUseCase
import com.example.tokitoki.domain.usecase.UpdateAgeUseCaseImpl
import com.example.tokitoki.domain.usecase.UpdateDatabaseUseCase
import com.example.tokitoki.domain.usecase.UpdateDatabaseUseCaseImpl
import com.example.tokitoki.domain.usecase.ValidateAuthCodeUseCase
import com.example.tokitoki.domain.usecase.ValidateAuthCodeUseCaseImpl
import com.example.tokitoki.domain.usecase.ValidateEmailFormatUseCase
import com.example.tokitoki.domain.usecase.ValidateEmailFormatUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    @Singleton
    abstract fun bindGetCategoriesUseCase(
        impl: GetCategoriesUseCaseImpl
    ): GetCategoriesUseCase

    @Binds
    @Singleton
    abstract fun bindGetMyProfileUseCase(
        impl: GetMyProfileUseCaseImpl
    ): GetMyProfileUseCase

    @Binds
    @Singleton
    abstract fun bindGetMySelfSentenceUseCase(
        impl: GetMySelfSentenceUseCaseImpl
    ): GetMySelfSentenceUseCase


    @Binds
    @Singleton
    abstract fun bindGetMyTagUseCase(
        impl: GetMyTagUseCaseImpl
    ): GetMyTagUseCase

    @Binds
    @Singleton
    abstract fun bindGetTagByCategoryIdUseCase(
        impl: GetTagByCategoryIdUseCaseImpl
    ): GetTagByCategoryIdUseCase

    @Binds
    @Singleton
    abstract fun bindGetTagByTagIdUseCase(
        impl: GetTagByTagIdUseCaseImpl
    ): GetTagByTagIdUseCase

    @Binds
    @Singleton
    abstract fun bindValidateAuthCodeUseCase(
        impl: ValidateAuthCodeUseCaseImpl
    ): ValidateAuthCodeUseCase

    @Binds
    @Singleton
    abstract fun bindValidateEmailFormatUseCase(
        impl: ValidateEmailFormatUseCaseImpl
    ): ValidateEmailFormatUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateDatabaseUseCase(
        impl: UpdateDatabaseUseCaseImpl
    ): UpdateDatabaseUseCase

    @Binds
    @Singleton
    abstract fun bindSetMyProfileUseCase(
        impl: SetMyProfileUseCaseImpl
    ): SetMyProfileUseCase

    @Binds
    @Singleton
    abstract fun bindCalculateAgeUseCase(
        impl: CalculateAgeUseCaseImpl
    ): CalculateAgeUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateAgeUseCase(
        impl: UpdateAgeUseCaseImpl
    ): UpdateAgeUseCase
}