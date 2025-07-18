package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.model.MyTag

interface MyProfileRepository {
    // User Profile 관련 메서드
    suspend fun getUserProfile(): MyProfile? // 유저 프로필 조회
    suspend fun saveUserProfile(profile: MyProfile) // 유저 프로필 저장 (업데이트 포함)
    suspend fun deleteUserProfile() // 유저 프로필 삭제

    // --- API 통신 관련 메서드 ---
    /** 원격 API로부터 사용자 프로필 정보를 가져옵니다. */
    suspend fun fetchUserProfileFromApi(): MyProfile

    // User Tags 관련 메서드
    suspend fun addUserTag(tag: MyTag): Boolean // 태그 추가
    suspend fun removeUserTag(tagId: Int) // 특정 태그 삭제
    suspend fun getUserTagsByTagTypeId(tagTypeId: Int): List<MyTag> // 모든 태그 ID 가져오기
    suspend fun getUserTags(): List<MyTag>
    suspend fun clearUserTags() // 모든 태그 삭제
    
    // Tag Subscription 관련 메서드
    suspend fun isTagSubscribed(tagId: String): Boolean // 태그 구독 여부 확인
}
