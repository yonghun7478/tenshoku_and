package com.example.tokitoki.data.repository

import com.example.tokitoki.data.local.MyProfileDao
import com.example.tokitoki.data.local.MyProfileEntity
import com.example.tokitoki.data.local.MyTagDao
import com.example.tokitoki.domain.converter.MyProfileConverter
import com.example.tokitoki.domain.converter.MyTagConverter
import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.model.MyTag
import com.example.tokitoki.domain.repository.MyProfileRepository
import javax.inject.Inject

class MyProfileRepositoryImpl @Inject constructor(
    private val myProfileDao: MyProfileDao,
    private val myTagDao: MyTagDao
) : MyProfileRepository {

    override suspend fun getUserProfile(): MyProfile? {
        return myProfileDao.getProfile()?.let { MyProfileConverter.entityToDomain(it) }
    }

    override suspend fun saveUserProfile(profile: MyProfile) {
        myProfileDao.insertOrUpdateProfile(MyProfileConverter.domainToEntity(profile))
    }

    override suspend fun deleteUserProfile() {
        myProfileDao.deleteProfile()
    }

    override suspend fun fetchUserProfileFromApi(): MyProfile {
        println("Repository: Creating dummy user profile entity...")
        // 1. 하드코딩된 더미 Entity 생성
        val dummyEntity = MyProfileEntity(
            id = 100, // 임의의 ID
            name = "더미 사용자",
            birthDay = "19950815",
            isMale = true,
            mySelfSentenceId = 99, // 임의의 자기소개 ID
            email = "dummy@example.com",
            thumbnailUrl = "https://placehold.co/200x200/E6E6FA/AAAAAA?text=Dummy" // 임의의 이미지 URL
        )
        println("Repository: Dummy entity created: $dummyEntity")

        // 2. Entity를 Domain 모델로 변환
        val domainProfile = MyProfileConverter.entityToDomain(dummyEntity)
        println("Repository: Converted dummy entity to domain: $domainProfile")

        // 3. 변환된 Domain 모델 반환
        return domainProfile
    }

    override suspend fun addUserTag(tag: MyTag): Boolean {
        val result = myTagDao.insertMyTag(
            MyTagConverter.domainToEntity(tag)
        )
        return result > 0
    }

    override suspend fun removeUserTag(tagId: Int) {
        myTagDao.deleteMyTag(tagId)
    }

    override suspend fun getUserTagsByTagTypeId(tagTypeId: Int): List<MyTag> {
        return myTagDao.getTagsByTagTypeId(tagTypeId = tagTypeId)
            .map { MyTagConverter.entityToDomain(it) }
    }

    override suspend fun getUserTags(): List<MyTag> {
        return myTagDao.getAllMyTags().map { MyTagConverter.entityToDomain(it) }
    }

    override suspend fun clearUserTags() {
        myTagDao.deleteAllTagsForUser()
    }

    override suspend fun isTagSubscribed(tagId: String): Boolean {
        return myTagDao.isTagExists(tagId.toInt())
    }
}
