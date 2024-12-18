package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.model.MyTag

interface MyProfileRepository {
    // User Profile 관련 메서드
    suspend fun getUserProfile(): MyProfile? // 유저 프로필 조회
    suspend fun saveUserProfile(profile: MyProfile) // 유저 프로필 저장 (업데이트 포함)
    suspend fun updateUserName(name: String) // 유저 이름 변경
    suspend fun updateUserBirthday(birthDay: String) // 유저 나이 변경
    suspend fun updateGender(isMale: Boolean)
    suspend fun updateMySelfSentence(sentenceId: Int) // 자기소개 변경
    suspend fun deleteUserProfile() // 유저 프로필 삭제

    // User Tags 관련 메서드
    suspend fun addUserTag(tag: MyTag): Boolean // 태그 추가
    suspend fun removeUserTag(tagId: Int) // 특정 태그 삭제
    suspend fun getUserTagsByCategoryId(categoryId: Int): List<MyTag> // 모든 태그 ID 가져오기
    suspend fun getUserTags(): List<MyTag>
    suspend fun clearUserTags() // 모든 태그 삭제
}
