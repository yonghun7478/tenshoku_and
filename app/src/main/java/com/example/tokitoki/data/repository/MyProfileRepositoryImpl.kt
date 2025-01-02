package com.example.tokitoki.data.repository

import com.example.tokitoki.data.local.MyProfileDao
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

    override suspend fun addUserTag(tag: MyTag): Boolean {
        val result = myTagDao.insertMyTag(
            MyTagConverter.domainToEntity(tag)
        )
        return result > 0
    }

    override suspend fun removeUserTag(tagId: Int) {
        myTagDao.deleteMyTag(tagId)
    }

    override suspend fun getUserTagsByCategoryId(categoryId: Int): List<MyTag> {
        return myTagDao.getTagsByCategoryId(categoryId = categoryId)
            .map { MyTagConverter.entityToDomain(it) }
    }

    override suspend fun getUserTags(): List<MyTag> {
        return myTagDao.getAllMyTags().map { MyTagConverter.entityToDomain(it) }
    }

    override suspend fun clearUserTags() {
        myTagDao.deleteAllTagsForUser()
    }
}
