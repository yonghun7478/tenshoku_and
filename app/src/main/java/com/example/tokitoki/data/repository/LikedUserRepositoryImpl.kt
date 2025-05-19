package com.example.tokitoki.data.repository

import com.example.tokitoki.data.mapper.toDomain
import com.example.tokitoki.data.model.LikedUserDto
import com.example.tokitoki.domain.model.LikedUser
import com.example.tokitoki.domain.repository.LikedUserRepository
import javax.inject.Inject
import kotlin.random.Random

class LikedUserRepositoryImpl @Inject constructor() : LikedUserRepository {
    // 더미 데이터를 저장할 맵 (userId to LikedUserDto)
    private val dummyUsers = mutableMapOf<String, LikedUserDto>()

    init {
        // 더미 데이터 초기화
        repeat(50) { index ->
            val userId = "$index"
            val currentTime = System.currentTimeMillis()
            val randomTimeOffset = Random.nextLong(0, 7 * 24 * 60 * 60 * 1000) // 최대 1주일 전
            dummyUsers[userId] = LikedUserDto(
                id = userId,
                nickname = "사용자$index",
                age = Random.nextInt(20, 40),
                location = listOf("서울", "부산", "대구", "인천", "광주").random(),
                profileImageUrl = "https://upload.wikimedia.org/wikipedia/commons/e/ee/2023_MMA_IVE_Wonyoung_1.jpg",
                introduction = if (Random.nextBoolean()) "안녕하세요! 저는 사용자${index}입니다." else null,
                occupation = if (Random.nextBoolean()) listOf("회사원", "학생", "프리랜서", "공무원").random() else null,
                likedAt = currentTime - randomTimeOffset
            )
        }
    }

    override suspend fun getLikedUsers(cursor: Long?, pageSize: Int): List<LikedUser> {
        return dummyUsers.values
            .asSequence()
            .filter { cursor == null || it.likedAt < cursor }
            .sortedByDescending { it.likedAt }
            .take(pageSize)
            .map { it.toDomain() }
            .toList()
    }

    override suspend fun updateLikeStatus(userId: String, isLiked: Boolean): Result<Unit> {
        return if (isLiked) {
            // 좋아요 추가
            if (!dummyUsers.containsKey(userId)) {
                val newUser = LikedUserDto(
                    id = userId,
                    nickname = "새로운 사용자",
                    age = Random.nextInt(20, 40),
                    location = listOf("서울", "부산", "대구", "인천", "광주").random(),
                    profileImageUrl = "https://example.com/profile/$userId.jpg",
                    introduction = null,
                    occupation = null,
                    likedAt = System.currentTimeMillis()
                )
                dummyUsers[userId] = newUser
            }
            Result.success(Unit)
        } else {
            // 좋아요 제거
            dummyUsers.remove(userId)
            Result.success(Unit)
        }
    }
} 