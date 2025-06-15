package com.example.tokitoki.data.dummy

import com.example.tokitoki.domain.model.UserDetail
import java.util.Calendar

object DummyData {

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

    private val dummyUserDetails: List<UserDetail>

    init {
        dummyUserDetails = (1..150).map { id ->
            createUserDetail(id.toString())
        }
    }
    
    fun getUsers(): List<UserDetail> = dummyUserDetails
    
    fun findUserById(id: String): UserDetail? {
        return dummyUserDetails.find { it.id == id }
    }

    private fun createUserDetail(userId: String): UserDetail {
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
        val age = (18..60).random()
        val birthYear = currentYear - age
        val userIdInt = userId.toIntOrNull() ?: 1

        return UserDetail(
            id = userId,
            name = "User${userId}",
            birthDay = "$birthYear-${String.format("%02d", (userIdInt % 12) + 1)}-${String.format("%02d", (userIdInt % 28) + 1)}",
            isMale = userIdInt % 2 == 0,
            email = "user${userId}@example.com",
            thumbnailUrl = thumbnailUrls.random(),
            age = age,
            location = locations[userIdInt % locations.size],
            introduction = introductions[userIdInt % introductions.size],
            bloodType = bloodTypes[userIdInt % bloodTypes.size],
            education = educations[userIdInt % educations.size],
            occupation = occupations[userIdInt % occupations.size],
            appearance = appearances[userIdInt % appearances.size],
            datingPhilosophy = datingPhilosophies[userIdInt % datingPhilosophies.size],
            marriageView = marriageViews[userIdInt % marriageViews.size],
            personalityTraits = allPersonalityTraits.shuffled().take((userIdInt % 2) + 2),
            hobbies = allHobbies.shuffled().take((userIdInt % 2) + 2),
            lifestyle = lifestyles[userIdInt % lifestyles.size],
            createdAt = (1609459200000..1672531199000).random(),
            lastLoginAt = (1672531200000..1704067199000).random()
        )
    }
} 