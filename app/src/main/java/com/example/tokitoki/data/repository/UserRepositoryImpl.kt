package com.example.tokitoki.data.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.data.dummy.DummyData
import com.example.tokitoki.data.local.UserEntity
import com.example.tokitoki.domain.converter.UserConverter
import com.example.tokitoki.domain.model.User
import com.example.tokitoki.domain.model.UserList
import com.example.tokitoki.domain.repository.UserRepository
import javax.inject.Inject
import com.example.tokitoki.domain.model.UserDetail
import java.util.Calendar
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {
    // 150개 더미 데이터 생성 (id는 1~150)
    // getTagSubscribers 에서 아직 사용중이므로 남겨둡니다.
    private val thumbnailUrls = listOf(
        "https://cdn.mhnse.com/news/photo/202411/354069_409655_3737.jpg",
        "https://img.hankyung.com/photo/202411/03.38739677.1.jpg",
        "https://image.newdaily.co.kr/site/data/img/2025/02/03/2025020300300_0.jpg",
        "https://cdn.hankooki.com/news/photo/202504/247606_344901_1744771524.jpg",
        "https://talkimg.imbc.com/TVianUpload/tvian/TViews/image/2025/02/03/140455b2-3135-4315-9ca1-9c68a7db7546.jpg",
        "https://www.cosinkorea.com/data/photos/20240414/art_17120529936565_2a8f34.jpg",
        "https://cdn.hankooki.com/news/photo/202503/243191_338487_1743185951.jpg",
        "https://img.hankyung.com/photo/202504/03.39959436.1.jpg"
    )

    private val allUsers: List<UserDetail> = DummyData.getUsers()

    override suspend fun getUsers(
        cursor: String?,
        limit: Int,
        orderBy: String
    ): ResultWrapper<UserList> {
        return try {
            // 정렬
            val sortedUsers = when (orderBy) {
                "lastLoginAt" -> allUsers.sortedByDescending { it.lastLoginAt }
                else -> allUsers.sortedByDescending { it.createdAt }
            }

            // cursor가 null이면 처음부터, 아니면 해당 id 이후부터
            val startIndex = cursor?.let { c ->
                sortedUsers.indexOfFirst { it.id == c } + 1
            } ?: 0

            val pagedUsers = sortedUsers.drop(startIndex).take(limit)

            val nextCursor = pagedUsers.lastOrNull()?.id
            val isLastPage = (startIndex + pagedUsers.size) >= sortedUsers.size

            ResultWrapper.Success(
                UserList(
                    users = pagedUsers.map { userDetail ->
                        User(
                            id = userDetail.id,
                            thumbnailUrl = userDetail.thumbnailUrl,
                            age = userDetail.age,
                        )
                    },
                    nextCursor = if (isLastPage) null else nextCursor,
                    isLastPage = isLastPage
                )
            )
        } catch (e: Exception) {
            ResultWrapper.Error(
                ResultWrapper.ErrorType.ExceptionError(
                    e.message ?: "Unknown error"
                )
            )
        }
    }

    override suspend fun getUserDetail(userId: String): ResultWrapper<UserDetail> {
        return try {
            val userDetail = DummyData.findUserById(userId)
            if (userDetail != null) {
                ResultWrapper.Success(userDetail)
            } else {
                ResultWrapper.Error(ResultWrapper.ErrorType.ExceptionError("User not found"))
            }
        } catch (e: Exception) {
            ResultWrapper.Error(ResultWrapper.ErrorType.ExceptionError(e.message ?: "Unknown error"))
        }
    }

    override suspend fun getUsersByIds(
        userIds: List<String>
    ): ResultWrapper<List<User>> {
        return try {
            val allUsers = DummyData.getUsers()
            val targetUsers = allUsers.filter { userIds.contains(it.id) }

            // lastLoginAt 기준으로 정렬
            val sortedUsers = targetUsers.sortedByDescending { it.lastLoginAt }

            ResultWrapper.Success(
                sortedUsers.map { userDetail ->
                    User(
                        id = userDetail.id,
                        thumbnailUrl = userDetail.thumbnailUrl,
                        age = userDetail.age,
                    )
                }
            )
        } catch (e: Exception) {
            ResultWrapper.Error(ResultWrapper.ErrorType.ExceptionError(e.message ?: "Unknown error"))
        }
    }

    override suspend fun sendMiten(userId: String): ResultWrapper<Unit> {
        return try {
            kotlinx.coroutines.delay(1000) 
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            ResultWrapper.Error(ResultWrapper.ErrorType.ExceptionError(e.message ?: "Unknown error"))
        }
    }
}