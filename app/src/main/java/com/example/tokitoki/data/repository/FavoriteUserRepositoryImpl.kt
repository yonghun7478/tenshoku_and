package com.example.tokitoki.data.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.common.ResultWrapper.ErrorType
import com.example.tokitoki.domain.model.FavoriteUser
import com.example.tokitoki.domain.repository.FavoriteUserRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class FavoriteUserRepositoryImpl @Inject constructor() : FavoriteUserRepository {

    private val dummyUsers = (1..30).map {
        FavoriteUser(
            id = it.toString(),
            thumbnailUrl = "https://upload.wikimedia.org/wikipedia/commons/e/ee/2023_MMA_IVE_Wonyoung_1.jpg",
            name = "User $it",
            age = (20..40).random(),
            location = "Seoul",
            height = (160..190).random(),
            job = "Developer",
            hasRoommate = it % 2 == 0,
            siblings = "1",
            bloodType = "A",
            timestamp = System.currentTimeMillis() - (30 - it) * 1000 // 예시 timestamp
        )
    }.sortedByDescending { it.timestamp } // 최신 timestamp 기준으로 정렬

    // 즐겨찾기 상태를 저장할 Set
    private val favoriteUserIds: MutableSet<String> = mutableSetOf()

    override suspend fun getFavoriteUsers(limit: Int, cursor: Long): List<FavoriteUser> {
        delay(500) // Simulate network delay
        // cursor가 0이면 첫 페이지, 아니면 해당 timestamp 이전의 데이터를 가져옴
        return if (cursor == 0L) {
            dummyUsers.take(limit)
        } else {
            dummyUsers.filter { it.timestamp < cursor }.take(limit)
        }
    }

    override suspend fun addToFavorites(userId: String): ResultWrapper<Unit> {
        return try {
            // TODO: 실제 API 호출 구현
            delay(500) // API 호출 시뮬레이션
            favoriteUserIds.add(userId) // 즐겨찾기 성공 시 userId를 세트에 추가
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            ResultWrapper.Error(ErrorType.ExceptionError(e.message ?: "즐겨찾기 추가 중 오류가 발생했습니다."))
        }
    }

    override suspend fun removeFromFavorites(userId: String): ResultWrapper<Unit> {
        return try {
            // TODO: 실제 API 호출 구현
            delay(500) // API 호출 시뮬레이션
            favoriteUserIds.remove(userId) // 즐겨찾기 제거 성공 시 userId를 세트에서 제거
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            ResultWrapper.Error(ErrorType.ExceptionError(e.message ?: "즐겨찾기 추가 중 오류가 발생했습니다."))
        }
    }

    override suspend fun isUserFavorite(userId: String): ResultWrapper<Boolean> {
        delay(100) // Simulate network delay
        // 더미 구현: favoriteUserIds 세트에 userId가 포함되어 있는지 반환
        return ResultWrapper.Success(favoriteUserIds.contains(userId))
    }
}