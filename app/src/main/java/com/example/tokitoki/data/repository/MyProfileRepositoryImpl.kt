package com.example.tokitoki.data.repository

import com.example.tokitoki.data.local.MyProfileDao
import com.example.tokitoki.data.local.MyTagDao
import com.example.tokitoki.data.local.MyTagEntity
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

    override suspend fun updateUserName(name: String) {
        myProfileDao.updateName(name)
    }

    override suspend fun updateUserBirthday(birthDay: String) {
        myProfileDao.updateBirthDay(birthDay)
    }

    override suspend fun updateGender(isMale: Boolean) {
        myProfileDao.updateGender(isMale)
    }

    override suspend fun updateMySelfSentence(sentence: String) {
        myProfileDao.updateMySelfSentence(sentence)
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

    override suspend fun getUserTags(): List<Int> {
        return myTagDao.getAllTagIds()
    }

    override suspend fun getUserTagsAsDomain(): List<MyTag> {
        val myTagEntities = listOf(
            MyTagEntity(tagId = 1),
            MyTagEntity(tagId = 2),
            MyTagEntity(tagId = 3)
        )
        return myTagEntities.map { MyTagConverter.entityToDomain(it) }
//        return myTagDao.getAllMyTags().map { MyTagConverter.entityToDomain(it) }
    }

    override suspend fun clearUserTags() {
        myTagDao.deleteAllTagsForUser()
    }
}
