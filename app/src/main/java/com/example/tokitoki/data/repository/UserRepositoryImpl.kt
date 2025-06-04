package com.example.tokitoki.data.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.data.local.UserEntity
import com.example.tokitoki.domain.converter.UserConverter
import com.example.tokitoki.domain.model.UserList
import com.example.tokitoki.domain.repository.UserRepository
import javax.inject.Inject
import com.example.tokitoki.domain.model.UserDetail
import java.util.Calendar
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {
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

    // 150개 더미 데이터 생성 (id는 1~150)
    private val dummyUsers: List<UserEntity> = (1..150).map { id ->
        UserEntity(
            id = id.toString(),
            thumbnailUrl = thumbnailUrls.random(),
            age = (18..60).random(),
            createdAt = (1609459200..1672531199).random().toLong(),
            lastLoginAt = (1672531200..1704067199).random().toLong()
        )
    }

    override suspend fun getUsers(
        cursor: String?,
        limit: Int,
        orderBy: String
    ): ResultWrapper<UserList> {
        return try {
            // 정렬
            val sortedUsers = when (orderBy) {
                "lastLoginAt" -> dummyUsers.sortedByDescending { it.lastLoginAt }
                else -> dummyUsers.sortedByDescending { it.createdAt }
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
                    users = pagedUsers.map(UserConverter::dataToDomain),
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
            val user = dummyUsers.find { it.id == userId }
            if (user != null) {
                // 더미 데이터 생성을 위한 목록들
                val locations = listOf("서울", "부산", "인천", "대구", "도쿄", "오사카", "뉴욕")
                val introductions = listOf(
                    "만나서 반갑습니다! 함께 좋은 시간 보내요.",
                    "새로운 인연을 찾고 있어요. 잘 부탁드립니다.",
                    "안녕하세요, 프로필 읽어주셔서 감사합니다! 대화 나눠봐요.",
                    "같이 맛있는거 먹으러 다니거나, 재밌는 영화보는거 좋아해요.",
                    "운동을 좋아하고, 새로운 경험을 즐기는 편입니다."
                )
                val bloodTypes = listOf("A형", "B형", "O형", "AB형", "기타")
                val educations = listOf("고등학교 졸업", "대학교 재학", "대학교 졸업", "대학원 재학", "석사 졸업", "박사 과정")
                val occupations = listOf("회사원", "학생", "프리랜서", "자영업", "개발자", "디자이너", "의료계", "교육계")
                val appearances = listOf("깔끔한 스타일", "캐주얼 선호", "댄디 스타일", "개성있는 편", "편안한 스타일")
                val datingPhilosophies = listOf("진지한 만남 추구", "좋은 친구부터 시작", "서로를 알아가는 과정이 중요", "함께 성장하는 연애")
                val marriageViews = listOf("결혼에 긍정적", "아직은 잘 모르겠어요", "좋은 인연이 있다면 고려", "결혼 생각 없음")
                
                val allPersonalityTraits = listOf("활발한", "차분한", "유머러스한", "진지한", "긍정적인", "현실적인", "사교적인", "내향적인", "섬세한", "털털한")
                val allHobbies = listOf("영화감상", "음악듣기", "독서", "운동", "여행", "요리", "게임", "산책", "사진찍기", "악기연주")
                val allMyTags = listOf("#일상", "#맛집탐방", "#여행스타그램", "#개발자", "#운동", "#독서", "#영화광", "#음악추천", "#댕댕이", "#냥집사", "#코딩", "#주말나들이")
                val lifestyles = listOf("규칙적인 생활을 중요시해요.", "자유로운 영혼입니다!", "주로 집에서 시간을 보내요.", "새로운 것을 배우는 것을 좋아해요.", "여행을 통해 에너지를 얻어요.")

                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val birthYear = currentYear - user.age
                
                val userIdInt = userId.toIntOrNull() ?: 1

                val detail = UserDetail(
                    id = user.id,
                    name = "User${user.id}",
                    birthDay = "$birthYear-${String.format("%02d", (userIdInt % 12) + 1)}-${String.format("%02d", (userIdInt % 28) + 1)}",
                    isMale = userIdInt % 2 == 0,
                    email = "user${user.id}@example.com",
                    thumbnailUrl = user.thumbnailUrl,
                    
                    age = user.age,
                    location = locations[userIdInt % locations.size],
                    myTags = allMyTags.shuffled().take((userIdInt % 3) + 2),
                    introduction = introductions[userIdInt % introductions.size],
                    bloodType = bloodTypes[userIdInt % bloodTypes.size],
                    education = educations[userIdInt % educations.size],
                    occupation = occupations[userIdInt % occupations.size],
                    appearance = appearances[userIdInt % appearances.size],
                    datingPhilosophy = datingPhilosophies[userIdInt % datingPhilosophies.size],
                    marriageView = marriageViews[userIdInt % marriageViews.size],
                    personalityTraits = allPersonalityTraits.shuffled().take((userIdInt % 2) + 2),
                    hobbies = allHobbies.shuffled().take((userIdInt % 2) + 2),
                    lifestyle = lifestyles[userIdInt % lifestyles.size]
                )
                ResultWrapper.Success(detail)
            } else {
                ResultWrapper.Error(ResultWrapper.ErrorType.ExceptionError("User not found for ID: $userId"))
            }
        } catch (e: Exception) {
            ResultWrapper.Error(ResultWrapper.ErrorType.ExceptionError(e.message ?: "Error fetching user detail for ID: $userId"))
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

    override suspend fun getTagSubscribers(
        tagId: String,
        cursor: String?,
        limit: Int
    ): ResultWrapper<UserList> {
        return try {
            kotlinx.coroutines.delay(500)

            val sortedUsers = dummyUsers.sortedByDescending { it.lastLoginAt }

            val startIndex = cursor?.let { c ->
                sortedUsers.indexOfFirst { it.id == c } + 1
            } ?: 0

            val pagedUsers = sortedUsers.drop(startIndex).take(limit)

            val nextCursor = pagedUsers.lastOrNull()?.id
            val isLastPage = (startIndex + pagedUsers.size) >= sortedUsers.size

            ResultWrapper.Success(
                UserList(
                    users = pagedUsers.map(UserConverter::dataToDomain),
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
}