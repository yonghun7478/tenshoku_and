package com.example.tokitoki.di

import com.example.tokitoki.data.repository.AshiatoRepositoryImpl
import com.example.tokitoki.data.repository.AuthRepositoryImpl
import com.example.tokitoki.data.repository.DbRepositoryImpl
import com.example.tokitoki.data.repository.FavoriteUserRepositoryImpl
import com.example.tokitoki.data.repository.LikeRepositoryImpl
import com.example.tokitoki.data.repository.MainHomeTagCategoryRepositoryImpl
import com.example.tokitoki.data.repository.MainHomeTagRepositoryImpl
import com.example.tokitoki.data.repository.MessageRepositoryImpl
import com.example.tokitoki.data.repository.MessageListRepositoryImpl
import com.example.tokitoki.data.repository.MyProfileRepositoryImpl
import com.example.tokitoki.data.repository.MySelfSentenceRepositoryImpl
import com.example.tokitoki.data.repository.PickupUserRepositoryImpl
import com.example.tokitoki.data.repository.TagRepositoryImpl
import com.example.tokitoki.data.repository.UserCacheRepositoryImpl
import com.example.tokitoki.data.repository.UserRepositoryImpl
import com.example.tokitoki.domain.repository.AshiatoRepository
import com.example.tokitoki.domain.repository.AuthRepository
import com.example.tokitoki.domain.repository.DbRepository
import com.example.tokitoki.domain.repository.FavoriteUserRepository
import com.example.tokitoki.domain.repository.LikeRepository
import com.example.tokitoki.domain.repository.MainHomeTagCategoryRepository
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import com.example.tokitoki.domain.repository.MessageRepository
import com.example.tokitoki.domain.repository.MessageListRepository
import com.example.tokitoki.domain.repository.MyProfileRepository
import com.example.tokitoki.domain.repository.MySelfSentenceRepository
import com.example.tokitoki.domain.repository.PickupUserRepository
import com.example.tokitoki.domain.repository.TagRepository
import com.example.tokitoki.domain.repository.UserCacheRepository
import com.example.tokitoki.domain.repository.UserRepository
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

    @Binds
    @Singleton
    abstract fun bindDbRepository(
        dbRepositoryImpl: DbRepositoryImpl
    ): DbRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindPickupUserRepository(
        pickupRepositoryImpl: PickupUserRepositoryImpl
    ): PickupUserRepository

    @Binds
    @Singleton
    abstract fun bindMainHomeTagRepository(
        mainHomeTagRepositoryImpl: MainHomeTagRepositoryImpl
    ): MainHomeTagRepository

    @Binds
    @Singleton
    abstract fun bindMainHomeTagCategoryRepository(
        mainHomeTagCategoryRepositoryImpl: MainHomeTagCategoryRepositoryImpl
    ): MainHomeTagCategoryRepository

    @Binds
    @Singleton
    abstract fun bindLikeRepository(repositoryImpl: LikeRepositoryImpl): LikeRepository

    @Binds
    @Singleton
    abstract fun bindMessageRepository(
        messageRepositoryImpl: MessageRepositoryImpl
    ): MessageRepository

    @Binds
    @Singleton
    abstract fun bindMessageListRepository(
        messageRepositoryImpl: MessageListRepositoryImpl
    ): MessageListRepository

    @Binds
    @Singleton
    abstract fun bindAshiatoRepository(
        ashiatoRepositoryImpl: AshiatoRepositoryImpl
    ): AshiatoRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteUserRepository(
        favoriteUserRepositoryImpl: FavoriteUserRepositoryImpl
    ): FavoriteUserRepository

    @Binds
    @Singleton
    abstract fun bindUserCacheRepository(
        userCacheRepositoryImpl: UserCacheRepositoryImpl
    ): UserCacheRepository
}