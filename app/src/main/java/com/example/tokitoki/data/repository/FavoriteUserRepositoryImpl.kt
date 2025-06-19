package com.example.tokitoki.data.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.common.ResultWrapper.ErrorType
import com.example.tokitoki.data.dummy.DummyData
import com.example.tokitoki.domain.model.FavoriteUser
import com.example.tokitoki.domain.repository.FavoriteUserRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class FavoriteUserRepositoryImpl @Inject constructor() : FavoriteUserRepository {

    private val allUsers: List<FavoriteUser>
    private val favoriteUserIds: MutableSet<String>

    init {
        val userDetails = DummyData.getUsers()
        allUsers = userDetails.map { userDetail ->
            FavoriteUser(
                id = userDetail.id,
                thumbnailUrl = userDetail.thumbnailUrl,
                name = userDetail.name,
                age = userDetail.age,
                location = userDetail.location,
                height = (160..190).random(),
                job = userDetail.occupation,
                hasRoommate = (userDetail.id.toIntOrNull() ?: 0) % 2 == 0,
                siblings = "1",
                bloodType = userDetail.bloodType,
                timestamp = userDetail.lastLoginAt
            )
        }.sortedByDescending { it.timestamp }

        favoriteUserIds = allUsers.take(5).map { it.id }.toMutableSet()
    }

    override suspend fun getFavoriteUsers(limit: Int, cursor: Long): List<FavoriteUser> {
        delay(500) // Simulate network delay
        // cursor가 0이면 첫 페이지, 아니면 해당 timestamp 이전의 데이터를 가져옴
        return if (cursor == 0L) {
            allUsers.take(limit)
        } else {
            allUsers.filter { it.timestamp < cursor }.take(limit)
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