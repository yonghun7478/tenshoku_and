package com.example.tokitoki.di

import com.example.tokitoki.data.repository.AshiatoRepositoryImpl
import com.example.tokitoki.data.repository.AuthRepositoryImpl
import com.example.tokitoki.data.repository.DbRepositoryImpl
import com.example.tokitoki.data.repository.FavoriteUserRepositoryImpl
import com.example.tokitoki.data.repository.LikeRepositoryImpl
import com.example.tokitoki.data.repository.LikedUserRepositoryImpl
import com.example.tokitoki.data.repository.MainHomeTagRepositoryImpl
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
import com.example.tokitoki.domain.repository.LikedUserRepository
import com.example.tokitoki.domain.repository.MainHomeTagRepository
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
        mainHomeTagRepositoryImpl: MainHomeTagRepositoryImpl // 구현체 클래스
    ): MainHomeTagRepository // 인터페이스

    @Binds
    @Singleton
    abstract fun bindLikeRepository(repositoryImpl: LikeRepositoryImpl): LikeRepository

    @Binds
    @Singleton
    abstract fun bindMessageRepository(
        messageRepositoryImpl: MessageListRepositoryImpl
    ): MessageListRepository

    @Binds
    @Singleton // 싱글톤 스코프 지정
    abstract fun bindAshiatoRepository(
        ashiatoRepositoryImpl: AshiatoRepositoryImpl // 실제 구현체 클래스
    ): AshiatoRepository // 제공할 인터페이스 타입

    @Binds
    @Singleton
    abstract fun bindLikedUserRepository(
        repository: LikedUserRepositoryImpl
    ): LikedUserRepository

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