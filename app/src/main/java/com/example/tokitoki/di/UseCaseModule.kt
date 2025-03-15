package com.example.tokitoki.di

import com.example.tokitoki.domain.usecase.AddRecentSearchUseCase
import com.example.tokitoki.domain.usecase.AddRecentSearchUseCaseImpl
import com.example.tokitoki.domain.usecase.AddSelectedTagUseCase
import com.example.tokitoki.domain.usecase.AddSelectedTagUseCaseImpl
import com.example.tokitoki.domain.usecase.CalculateAgeUseCase
import com.example.tokitoki.domain.usecase.CalculateAgeUseCaseImpl
import com.example.tokitoki.domain.usecase.CheckEmailRegisteredUseCase
import com.example.tokitoki.domain.usecase.CheckEmailRegisteredUseCaseImpl
import com.example.tokitoki.domain.usecase.ClearMyTagUseCase
import com.example.tokitoki.domain.usecase.ClearMyTagUseCaseImpl
import com.example.tokitoki.domain.usecase.DeleteRecentSearchUseCase
import com.example.tokitoki.domain.usecase.DeleteRecentSearchUseCaseImpl
import com.example.tokitoki.domain.usecase.DislikePickupUserUseCase
import com.example.tokitoki.domain.usecase.DislikePickupUserUseCaseImpl
import com.example.tokitoki.domain.usecase.FetchPickupUsersUseCase
import com.example.tokitoki.domain.usecase.FetchPickupUsersUseCaseImpl
import com.example.tokitoki.domain.usecase.GetCategoriesUseCase
import com.example.tokitoki.domain.usecase.GetCategoriesUseCaseImpl
import com.example.tokitoki.domain.usecase.GetMyProfileUseCase
import com.example.tokitoki.domain.usecase.GetMyProfileUseCaseImpl
import com.example.tokitoki.domain.usecase.GetAllMySelfSentenceUseCase
import com.example.tokitoki.domain.usecase.GetAllMySelfSentenceUseCaseImpl
import com.example.tokitoki.domain.usecase.GetLikesUseCase
import com.example.tokitoki.domain.usecase.GetLikesUseCaseImpl
import com.example.tokitoki.domain.usecase.GetMyMainHomeTagsUseCase
import com.example.tokitoki.domain.usecase.GetMyMainHomeTagsUseCaseImpl
import com.example.tokitoki.domain.usecase.GetMySelfSentenceUseCase
import com.example.tokitoki.domain.usecase.GetMySelfSentenceUseCaseImpl
import com.example.tokitoki.domain.usecase.GetMyTagUseCase
import com.example.tokitoki.domain.usecase.GetMyTagUseCaseImpl
import com.example.tokitoki.domain.usecase.GetRecentSearchesUseCase
import com.example.tokitoki.domain.usecase.GetRecentSearchesUseCaseImpl
import com.example.tokitoki.domain.usecase.GetRegistrationTokenUseCase
import com.example.tokitoki.domain.usecase.GetRegistrationTokenUseCaseImpl
import com.example.tokitoki.domain.usecase.GetSelectedTagsUseCase
import com.example.tokitoki.domain.usecase.GetSelectedTagsUseCaseImpl
import com.example.tokitoki.domain.usecase.GetSuggestedTagsUseCase
import com.example.tokitoki.domain.usecase.GetSuggestedTagsUseCaseImpl
import com.example.tokitoki.domain.usecase.GetTagByCategoryIdUseCase
import com.example.tokitoki.domain.usecase.GetTagByCategoryIdUseCaseImpl
import com.example.tokitoki.domain.usecase.GetTagByTagIdWithCategoryIdUseCase
import com.example.tokitoki.domain.usecase.GetTagByTagIdWithCategoryIdUseCaseImpl
import com.example.tokitoki.domain.usecase.GetTodayTagUseCase
import com.example.tokitoki.domain.usecase.GetTodayTagUseCaseImpl
import com.example.tokitoki.domain.usecase.GetTokensUseCase
import com.example.tokitoki.domain.usecase.GetTokensUseCaseImpl
import com.example.tokitoki.domain.usecase.GetTrendingTagsUseCase
import com.example.tokitoki.domain.usecase.GetTrendingTagsUseCaseImpl
import com.example.tokitoki.domain.usecase.GetUsersByLoginUseCase
import com.example.tokitoki.domain.usecase.GetUsersByLoginUseCaseImpl
import com.example.tokitoki.domain.usecase.GetUsersBySignupUseCase
import com.example.tokitoki.domain.usecase.GetUsersBySignupUseCaseImpl
import com.example.tokitoki.domain.usecase.LikePickupUserUseCase
import com.example.tokitoki.domain.usecase.LikePickupUserUseCaseImpl
import com.example.tokitoki.domain.usecase.RegisterMyProfileUseCase
import com.example.tokitoki.domain.usecase.RegisterMyProfileUseCaseImpl
import com.example.tokitoki.domain.usecase.RemoveSelectedTagUseCase
import com.example.tokitoki.domain.usecase.RemoveSelectedTagUseCaseImpl
import com.example.tokitoki.domain.usecase.RestoreTempSelectedTagsUseCase
import com.example.tokitoki.domain.usecase.RestoreTempSelectedTagsUseCaseImpl
import com.example.tokitoki.domain.usecase.SaveRegistrationTokenUseCase
import com.example.tokitoki.domain.usecase.SaveRegistrationTokenUseCaseImpl
import com.example.tokitoki.domain.usecase.SaveTempSelectedTagsUseCase
import com.example.tokitoki.domain.usecase.SaveTempSelectedTagsUseCaseImpl
import com.example.tokitoki.domain.usecase.SaveTokensUseCase
import com.example.tokitoki.domain.usecase.SaveTokensUseCaseImpl
import com.example.tokitoki.domain.usecase.SearchTagsUseCase
import com.example.tokitoki.domain.usecase.SearchTagsUseCaseImpl
import com.example.tokitoki.domain.usecase.VerifyGoogleTokenUseCase
import com.example.tokitoki.domain.usecase.VerifyGoogleTokenUseCaseImpl
import com.example.tokitoki.domain.usecase.VerifyEmailUseCase
import com.example.tokitoki.domain.usecase.VerifyEmailUseCaseImpl
import com.example.tokitoki.domain.usecase.SetMyProfileUseCase
import com.example.tokitoki.domain.usecase.SetMyProfileUseCaseImpl
import com.example.tokitoki.domain.usecase.SetMyTagUseCase
import com.example.tokitoki.domain.usecase.SetMyTagUseCaseImpl
import com.example.tokitoki.domain.usecase.UpdateDatabaseUseCase
import com.example.tokitoki.domain.usecase.UpdateDatabaseUseCaseImpl
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
    abstract fun bindGetAllMySelfSentenceUseCase(
        impl: GetAllMySelfSentenceUseCaseImpl
    ): GetAllMySelfSentenceUseCase

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
        impl: GetTagByTagIdWithCategoryIdUseCaseImpl
    ): GetTagByTagIdWithCategoryIdUseCase

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
    abstract fun bindSetMyTagUseCase(
        impl: SetMyTagUseCaseImpl
    ): SetMyTagUseCase

    @Binds
    @Singleton
    abstract fun bindClearMyTagUseCase(
        impl: ClearMyTagUseCaseImpl
    ): ClearMyTagUseCase

    @Binds
    @Singleton
    abstract fun bindVerifyEmailUseCase(
        impl: VerifyEmailUseCaseImpl
    ): VerifyEmailUseCase

    @Binds
    @Singleton
    abstract fun bindSaveTokensUseCase(
        impl: SaveTokensUseCaseImpl
    ): SaveTokensUseCase

    @Binds
    @Singleton
    abstract fun bindGetTokensUseCase(
        impl: GetTokensUseCaseImpl
    ): GetTokensUseCase

    @Binds
    @Singleton
    abstract fun bindRegisterMyProfileUseCase(
        impl: RegisterMyProfileUseCaseImpl
    ): RegisterMyProfileUseCase

    @Binds
    abstract fun bindSendGoogleTokenUseCase(
        useCaseImpl: VerifyGoogleTokenUseCaseImpl
    ): VerifyGoogleTokenUseCase

    @Binds
    @Singleton
    abstract fun bindSaveRegistrationTokenUseCase(
        impl: SaveRegistrationTokenUseCaseImpl
    ): SaveRegistrationTokenUseCase

    @Binds
    abstract fun bindGetRegistrationTokenUseCase(
        useCaseImpl: GetRegistrationTokenUseCaseImpl
    ): GetRegistrationTokenUseCase

    @Binds
    abstract fun bindCheckEmailRegisteredUseCase(
        useCaseImpl: CheckEmailRegisteredUseCaseImpl
    ): CheckEmailRegisteredUseCase

    @Binds
    abstract fun bindGetUsersByLoginUseCase(
        useCaseImpl: GetUsersByLoginUseCaseImpl
    ): GetUsersByLoginUseCase

    @Binds
    abstract fun bindGetUsersBySignupUseCase(
        useCaseImpl: GetUsersBySignupUseCaseImpl
    ): GetUsersBySignupUseCase

    @Binds
    abstract fun bindFetchPickupUsersUseCase(
        useCaseImpl: FetchPickupUsersUseCaseImpl
    ): FetchPickupUsersUseCase

    @Binds
    abstract fun bindLikePickupUserUseCase(
        useCaseImpl: LikePickupUserUseCaseImpl
    ): LikePickupUserUseCase

    @Binds
    abstract fun bindDislikePickupUserUseCase(
        useCaseImpl: DislikePickupUserUseCaseImpl
    ): DislikePickupUserUseCase

    @Binds
    abstract fun bindGetTodayTagUseCase(
        getTodayTagUseCaseImpl: GetTodayTagUseCaseImpl
    ): GetTodayTagUseCase

    @Binds
    abstract fun bindGetTrendingTagsUseCase(
        getTrendingTagsUseCaseImpl: GetTrendingTagsUseCaseImpl
    ): GetTrendingTagsUseCase

    @Binds
    abstract fun bindGetMyMainHomeTagsUseCase(
        getMyTagsUseCaseImpl: GetMyMainHomeTagsUseCaseImpl
    ): GetMyMainHomeTagsUseCase

    @Binds
    abstract fun bindGetSuggestedTagsUseCase(
        getSuggestedTagsUseCaseImpl: GetSuggestedTagsUseCaseImpl
    ): GetSuggestedTagsUseCase

    @Binds
    abstract fun bindSearchTagsUseCase(
        searchTagsUseCaseImpl: SearchTagsUseCaseImpl
    ): SearchTagsUseCase

    @Binds
    abstract fun bindGetRecentSearchesUseCase(
        getRecentSearchesUseCaseImpl: GetRecentSearchesUseCaseImpl
    ): GetRecentSearchesUseCase
    @Binds
    abstract fun bindAddRecentSearchUseCase(
        addRecentSearchUseCaseImpl: AddRecentSearchUseCaseImpl
    ): AddRecentSearchUseCase

    @Binds
    abstract fun bindDeleteRecentSearchUseCase(
        deleteRecentSearchUseCaseImpl: DeleteRecentSearchUseCaseImpl
    ): DeleteRecentSearchUseCase
    @Binds
    abstract fun bindAddSelectedTagUseCase(
        addSelectedTagUseCaseImpl: AddSelectedTagUseCaseImpl
    ): AddSelectedTagUseCase

    @Binds
    abstract fun bindRemoveSelectedTagUseCase(
        removeSelectedTagUseCaseImpl: RemoveSelectedTagUseCaseImpl
    ): RemoveSelectedTagUseCase

    @Binds
    abstract fun bindGetSelectedTagsUseCase(
        getSelectedTagsUseCaseImpl: GetSelectedTagsUseCaseImpl
    ): GetSelectedTagsUseCase
    @Binds
    abstract fun bindSaveTempSelectedTagsUseCase(
        saveTempSelectedTagsUseCaseImpl: SaveTempSelectedTagsUseCaseImpl
    ): SaveTempSelectedTagsUseCase
    @Binds
    abstract fun bindRestoreTempSelectedTagsUseCase(
        restoreTempSelectedTagsUseCaseImpl: RestoreTempSelectedTagsUseCaseImpl
    ): RestoreTempSelectedTagsUseCase

    @Binds
    abstract fun bindGetLikesUseCase(useCase: GetLikesUseCaseImpl): GetLikesUseCase
}