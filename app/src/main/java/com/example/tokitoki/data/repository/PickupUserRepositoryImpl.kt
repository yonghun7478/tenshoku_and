package com.example.tokitoki.data.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.data.model.PickupUserResponse
import com.example.tokitoki.domain.converter.PickupUserConverter
import com.example.tokitoki.domain.model.PickupUser
import com.example.tokitoki.domain.repository.PickupUserRepository
import javax.inject.Inject

class PickupUserRepositoryImpl @Inject constructor() : PickupUserRepository {
    override suspend fun fetchPickupUsers(): ResultWrapper<List<PickupUser>> {
        val mockData = listOf(
            PickupUserResponse("1", "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg", 25, "田中 花子", "東京都"),
            PickupUserResponse("2", "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg", 28, "佐藤 太郎", "大阪府"),
            PickupUserResponse("3", "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg", 23, "鈴木 美咲", "福岡県"),
            PickupUserResponse("4", "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg", 27, "高橋 健太", "北海道"),
            PickupUserResponse("5", "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg", 24, "伊藤 優子", "愛知県"),
            PickupUserResponse("6", "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg", 26, "渡辺 直樹", "神奈川県"),
        ).map { PickupUserConverter.fromResponse(it) }
        return ResultWrapper.Success(mockData)
    }

    override suspend fun likePickupUser(pickupUserId: String): ResultWrapper<Unit> {
        return ResultWrapper.Success(Unit)
    }

    override suspend fun dislikePickupUser(pickupUserId: String): ResultWrapper<Unit> {
        return ResultWrapper.Success(Unit)
    }
}