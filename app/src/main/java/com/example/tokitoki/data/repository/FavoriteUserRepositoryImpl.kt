package com.example.tokitoki.data.repository

import com.example.tokitoki.domain.model.FavoriteUser
import com.example.tokitoki.domain.repository.FavoriteUserRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class FavoriteUserRepositoryImpl @Inject constructor() : FavoriteUserRepository {

    private val dummyUsers = (1..30).map {
        FavoriteUser(
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

    override suspend fun getFavoriteUsers(limit: Int, cursor: Long): List<FavoriteUser> {
        delay(500) // Simulate network delay
        // cursor가 0이면 첫 페이지, 아니면 해당 timestamp 이전의 데이터를 가져옴
        return if (cursor == 0L) {
            dummyUsers.take(limit)
        } else {
            dummyUsers.filter { it.timestamp < cursor }.take(limit)
        }
    }
}