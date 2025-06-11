package com.example.tokitoki.di

import com.example.tokitoki.domain.usecase.CalculateAgeUseCase
import com.example.tokitoki.domain.usecase.CalculateAgeUseCaseImpl
import com.example.tokitoki.domain.usecase.CheckEmailRegisteredUseCase
import com.example.tokitoki.domain.usecase.CheckEmailRegisteredUseCaseImpl
import com.example.tokitoki.domain.usecase.ClearMyTagUseCase
import com.example.tokitoki.domain.usecase.ClearMyTagUseCaseImpl
import com.example.tokitoki.domain.usecase.DislikePickupUserUseCase
import com.example.tokitoki.domain.usecase.DislikePickupUserUseCaseImpl
import com.example.tokitoki.domain.usecase.FetchMyProfileUseCase
import com.example.tokitoki.domain.usecase.FetchMyProfileUseCaseImpl
import com.example.tokitoki.domain.usecase.FetchPickupUsersUseCase
import com.example.tokitoki.domain.usecase.FetchPickupUsersUseCaseImpl
import com.example.tokitoki.domain.usecase.GetMyProfileUseCase
import com.example.tokitoki.domain.usecase.GetMyProfileUseCaseImpl
import com.example.tokitoki.domain.usecase.GetAllMySelfSentenceUseCase
import com.example.tokitoki.domain.usecase.GetAllMySelfSentenceUseCaseImpl
import com.example.tokitoki.domain.usecase.GetAshiatoPageUseCase
import com.example.tokitoki.domain.usecase.GetAshiatoPageUseCaseImpl
import com.example.tokitoki.domain.usecase.GetFavoriteUsersUseCase
import com.example.tokitoki.domain.usecase.GetFavoriteUsersUseCaseImpl
import com.example.tokitoki.domain.usecase.GetLikesUseCase
import com.example.tokitoki.domain.usecase.GetLikesUseCaseImpl
import com.example.tokitoki.domain.usecase.GetMatchingUsersUseCase
import com.example.tokitoki.domain.usecase.GetMatchingUsersUseCaseImpl
import com.example.tokitoki.domain.usecase.GetMyMainHomeTagsUseCase
import com.example.tokitoki.domain.usecase.GetMyMainHomeTagsUseCaseImpl
import com.example.tokitoki.domain.usecase.GetMySelfSentenceUseCase
import com.example.tokitoki.domain.usecase.GetMySelfSentenceUseCaseImpl
import com.example.tokitoki.domain.usecase.GetMyTagUseCase
import com.example.tokitoki.domain.usecase.GetMyTagUseCaseImpl
import com.example.tokitoki.domain.usecase.GetPreviousChatsUseCase
import com.example.tokitoki.domain.usecase.GetPreviousChatsUseCaseImpl
import com.example.tokitoki.domain.usecase.GetRegistrationTokenUseCase
import com.example.tokitoki.domain.usecase.GetRegistrationTokenUseCaseImpl
import com.example.tokitoki.domain.usecase.GetSuggestedTagsUseCase
import com.example.tokitoki.domain.usecase.GetSuggestedTagsUseCaseImpl
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
import com.example.tokitoki.domain.usecase.SaveRegistrationTokenUseCase
import com.example.tokitoki.domain.usecase.SaveRegistrationTokenUseCaseImpl
import com.example.tokitoki.domain.usecase.SaveTokensUseCase
import com.example.tokitoki.domain.usecase.SaveTokensUseCaseImpl
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
import com.example.tokitoki.domain.usecase.ClearTokensUseCase
import com.example.tokitoki.domain.usecase.ClearTokensUseCaseImpl
import com.example.tokitoki.domain.usecase.DeleteUserProfileUseCase
import com.example.tokitoki.domain.usecase.DeleteUserProfileUseCaseImpl
import com.example.tokitoki.domain.usecase.GetUserDetailUseCase
import com.example.tokitoki.domain.usecase.GetUserDetailUseCaseImpl
import com.example.tokitoki.domain.usecase.ClearCachedUserIdsUseCase
import com.example.tokitoki.domain.usecase.ClearCachedUserIdsUseCaseImpl
import com.example.tokitoki.domain.usecase.GetCachedUserIdsUseCase
import com.example.tokitoki.domain.usecase.GetCachedUserIdsUseCaseImpl
import com.example.tokitoki.domain.usecase.AddUserIdsToCacheUseCase
import com.example.tokitoki.domain.usecase.AddUserIdsToCacheUseCaseImpl
import com.example.tokitoki.domain.usecase.AddUserDetailToCacheUseCase
import com.example.tokitoki.domain.usecase.AddUserDetailToCacheUseCaseImpl
import com.example.tokitoki.domain.usecase.GetUserDetailFromCacheUseCase
import com.example.tokitoki.domain.usecase.GetUserDetailFromCacheUseCaseImpl
import com.example.tokitoki.domain.usecase.SendMitenUseCase
import com.example.tokitoki.domain.usecase.SendMitenUseCaseImpl
import com.example.tokitoki.domain.usecase.LikeUserUseCase
import com.example.tokitoki.domain.usecase.LikeUserUseCaseImpl
import com.example.tokitoki.domain.usecase.AddToFavoritesUseCase
import com.example.tokitoki.domain.usecase.AddToFavoritesUseCaseImpl
import com.example.tokitoki.domain.usecase.RemoveFromFavoritesUseCase
import com.example.tokitoki.domain.usecase.RemoveFromFavoritesUseCaseImpl
import com.example.tokitoki.domain.usecase.GetTagCategoriesUseCase
import com.example.tokitoki.domain.usecase.GetTagCategoriesUseCaseImpl
import com.example.tokitoki.domain.usecase.GetTagsByCategoryUseCase
import com.example.tokitoki.domain.usecase.GetTagsByCategoryUseCaseImpl
import com.example.tokitoki.domain.usecase.tag.GetTagsByQueryUseCase
import com.example.tokitoki.domain.usecase.tag.GetTagsByQueryUseCaseImpl
import com.example.tokitoki.domain.usecase.GetMyTagsByTypeUseCase
import com.example.tokitoki.domain.usecase.GetMyTagsByTypeUseCaseImpl
import com.example.tokitoki.domain.usecase.GetTagDetailUseCase
import com.example.tokitoki.domain.usecase.GetTagDetailUseCaseImpl
import com.example.tokitoki.domain.usecase.IsTagSubscribedUseCase
import com.example.tokitoki.domain.usecase.GetTagSubscribersUseCase
import com.example.tokitoki.domain.usecase.GetTagSubscribersUseCaseImpl
import com.example.tokitoki.domain.usecase.IsTagSubscribedUseCaseImpl
import com.example.tokitoki.domain.usecase.SubscribeTagUseCase
import com.example.tokitoki.domain.usecase.SubscribeTagUseCaseImpl
import com.example.tokitoki.domain.usecase.UnsubscribeTagUseCase
import com.example.tokitoki.domain.usecase.UnsubscribeTagUseCaseImpl
import com.example.tokitoki.domain.usecase.message.GetMessageHistoryUseCase
import com.example.tokitoki.domain.usecase.message.GetMessageHistoryUseCaseImpl
import com.example.tokitoki.domain.usecase.MoveMessageToPreviousUseCase
import com.example.tokitoki.domain.usecase.MoveMessageToPreviousUseCaseImpl
import com.example.tokitoki.domain.usecase.message.SendMessageUseCase
import com.example.tokitoki.domain.usecase.message.SendMessageUseCaseImpl
import com.example.tokitoki.domain.usecase.message.ReceiveMessageUseCase
import com.example.tokitoki.domain.usecase.message.ReceiveMessageUseCaseImpl
import com.example.tokitoki.domain.usecase.message.UpdateMessageStatusUseCase
import com.example.tokitoki.domain.usecase.message.UpdateMessageStatusUseCaseImpl
import com.example.tokitoki.domain.usecase.CheckIsUserLikedUseCase
import com.example.tokitoki.domain.usecase.CheckIsUserLikedUseCaseImpl
import com.example.tokitoki.domain.usecase.CheckIsUserFavoriteUseCase
import com.example.tokitoki.domain.usecase.CheckIsUserFavoriteUseCaseImpl
import com.example.tokitoki.domain.usecase.tag.GetUserTagsUseCase
import com.example.tokitoki.domain.usecase.tag.GetUserTagsUseCaseImpl
import com.example.tokitoki.domain.usecase.tag.GetTagsByTypeUseCase
import com.example.tokitoki.domain.usecase.tag.GetTagsByTypeUseCaseImpl
import com.example.tokitoki.domain.usecase.tag.GetTagTypeListUseCase
import com.example.tokitoki.domain.usecase.tag.GetTagTypeListUseCaseImpl
import com.example.tokitoki.domain.usecase.tag.GetTagsUseCase
import com.example.tokitoki.domain.usecase.tag.GetTagsUseCaseImpl
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
    abstract fun bindGetTagCategoriesUseCase(
        impl: GetTagCategoriesUseCaseImpl
    ): GetTagCategoriesUseCase

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
    abstract fun bindGetLikesUseCase(
        impl: GetLikesUseCaseImpl
    ): GetLikesUseCase

    @Binds
    abstract fun bindGetMatchingUsersUseCase(
        useCaseImpl: GetMatchingUsersUseCaseImpl
    ): GetMatchingUsersUseCase

    @Binds
    abstract fun bindGetPreviousChatsUseCase(
        useCaseImpl: GetPreviousChatsUseCaseImpl
    ): GetPreviousChatsUseCase

    @Binds
    abstract fun bindFetchMyProfileUseCase(
        impl: FetchMyProfileUseCaseImpl
    ): FetchMyProfileUseCase

    @Binds
    abstract fun bindGetAshiatoPageUseCase(
        useCaseImpl: GetAshiatoPageUseCaseImpl // 실제 구현체 클래스
    ): GetAshiatoPageUseCase // 제공할 인터페이스 타입

    @Binds
    abstract fun bindGetFavoriteUsersUseCase(
        useCaseImpl: GetFavoriteUsersUseCaseImpl
    ): GetFavoriteUsersUseCase

    @Binds
    @Singleton
    abstract fun bindClearTokensUseCase(
        impl: ClearTokensUseCaseImpl
    ): ClearTokensUseCase

    @Binds
    @Singleton
    abstract fun bindDeleteUserProfileUseCase(
        impl: DeleteUserProfileUseCaseImpl
    ): DeleteUserProfileUseCase

    @Binds
    @Singleton
    abstract fun bindGetUserDetailUseCase(
        impl: GetUserDetailUseCaseImpl
    ): GetUserDetailUseCase

    @Binds
    @Singleton
    abstract fun bindClearCachedUserIdsUseCase(
        impl: ClearCachedUserIdsUseCaseImpl
    ): ClearCachedUserIdsUseCase

    @Binds
    @Singleton
    abstract fun bindGetCachedUserIdsUseCase(
        impl: GetCachedUserIdsUseCaseImpl
    ): GetCachedUserIdsUseCase

    @Binds
    @Singleton
    abstract fun bindAddUserIdsToCacheUseCase(
        impl: AddUserIdsToCacheUseCaseImpl
    ): AddUserIdsToCacheUseCase

    @Binds
    @Singleton
    abstract fun bindAddUserDetailToCacheUseCase(
        impl: AddUserDetailToCacheUseCaseImpl
    ): AddUserDetailToCacheUseCase

    @Binds
    @Singleton
    abstract fun bindGetUserDetailFromCacheUseCase(
        impl: GetUserDetailFromCacheUseCaseImpl
    ): GetUserDetailFromCacheUseCase

    @Binds
    @Singleton
    abstract fun bindSendMitenUseCase(
        sendMitenUseCaseImpl: SendMitenUseCaseImpl
    ): SendMitenUseCase

    @Binds
    @Singleton
    abstract fun bindLikeUserUseCase(
        impl: LikeUserUseCaseImpl
    ): LikeUserUseCase

    @Binds
    @Singleton
    abstract fun bindAddToFavoritesUseCase(
        impl: AddToFavoritesUseCaseImpl
    ): AddToFavoritesUseCase

    @Binds
    @Singleton
    abstract fun bindRemoveFromFavoritesUseCase(
        impl: RemoveFromFavoritesUseCaseImpl
    ): RemoveFromFavoritesUseCase

    @Binds
    @Singleton
    abstract fun bindCheckIsUserLikedUseCase(
        impl: CheckIsUserLikedUseCaseImpl
    ): CheckIsUserLikedUseCase

    @Binds
    @Singleton
    abstract fun bindCheckIsUserFavoriteUseCase(
        impl: CheckIsUserFavoriteUseCaseImpl
    ): CheckIsUserFavoriteUseCase

    @Binds
    @Singleton
    abstract fun bindGetTagsByQueryUseCase(
        impl: GetTagsByQueryUseCaseImpl
    ): GetTagsByQueryUseCase

    @Binds
    @Singleton
    abstract fun bindGetTagsByCategoryUseCase(
        impl: GetTagsByCategoryUseCaseImpl
    ): GetTagsByCategoryUseCase

    @Binds
    @Singleton
    abstract fun bindGetMyTagsByTypeUseCase(
        impl: GetMyTagsByTypeUseCaseImpl
    ): GetMyTagsByTypeUseCase

    @Binds
    @Singleton
    abstract fun bindGetTagDetailUseCase(
        impl: GetTagDetailUseCaseImpl
    ): GetTagDetailUseCase

    @Binds
    @Singleton
    abstract fun bindIsTagSubscribedUseCase(
        impl: IsTagSubscribedUseCaseImpl
    ): IsTagSubscribedUseCase

    @Binds
    @Singleton
    abstract fun bindGetTagSubscribersUseCase(
        impl: GetTagSubscribersUseCaseImpl
    ): GetTagSubscribersUseCase

    @Binds
    @Singleton
    abstract fun bindSubscribeTagUseCase(
        impl: SubscribeTagUseCaseImpl
    ): SubscribeTagUseCase

    @Binds
    @Singleton
    abstract fun bindUnsubscribeTagUseCase(
        impl: UnsubscribeTagUseCaseImpl
    ): UnsubscribeTagUseCase

    @Binds
    @Singleton
    abstract fun bindGetMessageHistoryUseCase(
        impl: GetMessageHistoryUseCaseImpl
    ): GetMessageHistoryUseCase

    @Binds
    @Singleton
    abstract fun bindSendMessageUseCase(
        impl: SendMessageUseCaseImpl
    ): SendMessageUseCase

    @Binds
    @Singleton
    abstract fun bindReceiveMessageUseCase(
        impl: ReceiveMessageUseCaseImpl
    ): ReceiveMessageUseCase

    @Binds
    @Singleton
    abstract fun bindMoveMessageToPreviousUseCase(
        impl: MoveMessageToPreviousUseCaseImpl
    ): MoveMessageToPreviousUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateMessageStatusUseCase(
        impl: UpdateMessageStatusUseCaseImpl
    ): UpdateMessageStatusUseCase

    @Binds
    @Singleton
    abstract fun bindGetUserTagsUseCase(
        impl: GetUserTagsUseCaseImpl
    ): GetUserTagsUseCase

    @Binds
    @Singleton
    abstract fun bindGetTagsByTypeUseCase(
        impl: GetTagsByTypeUseCaseImpl
    ): GetTagsByTypeUseCase

    @Binds
    @Singleton
    abstract fun bindGetTagTypeListUseCase(
        impl: GetTagTypeListUseCaseImpl
    ): GetTagTypeListUseCase

    @Binds
    @Singleton
    abstract fun bindGetTagsUseCase(
        impl: GetTagsUseCaseImpl
    ): GetTagsUseCase
}
