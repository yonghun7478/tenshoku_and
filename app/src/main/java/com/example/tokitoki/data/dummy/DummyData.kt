package com.example.tokitoki.data.dummy

import com.example.tokitoki.domain.model.UserDetail
import java.util.Calendar
import com.example.tokitoki.data.model.MainHomeTagCategoryData
import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.model.TagType
import kotlin.random.Random

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
    private val dummyHomeTags: List<MainHomeTag>

    val usersForMatching: List<UserDetail>
    val usersWithPreviousChat: List<UserDetail>

    init {
        val allUsers = (1..150).map { id ->
            createUserDetail(id.toString())
        }.shuffled(Random(System.currentTimeMillis())) // 매번 다른 유저 목록을 위해 셔플

        val midPoint = allUsers.size / 2
        usersWithPreviousChat = allUsers.subList(0, midPoint)
        usersForMatching = allUsers.subList(midPoint, allUsers.size)


        dummyUserDetails = allUsers
        dummyHomeTags = createHomeTags()
    }
    
    val dummyMainHomeTagCategories = listOf(
        MainHomeTagCategoryData(
            id = "1",
            name = "趣味・スポーツ",
            description = "趣味やスポーツに関するタグ",
            imageUrl = "https://picsum.photos/id/1/100/100"
        ),
        MainHomeTagCategoryData(
            id = "2",
            name = "食べ物・飲み물",
            description = "食べ物や飲み物に関するタグ",
            imageUrl = "https://picsum.photos/id/2/100/100"
        ),
        MainHomeTagCategoryData(
            id = "3",
            name = "旅行・観光",
            description = "旅行や観光に関するタグ",
            imageUrl = "https://picsum.photos/id/3/100/100"
        ),
        MainHomeTagCategoryData(
            id = "4",
            name = "音楽・映画",
            description = "音楽や映画に関するタグ",
            imageUrl = "https://picsum.photos/id/4/100/100"
        ),
        MainHomeTagCategoryData(
            id = "5",
            name = "ファッション・美容",
            description = "ファッションや美容に関するタグ",
            imageUrl = "https://picsum.photos/id/5/100/100"
        ),
        MainHomeTagCategoryData(
            id = "6",
            name = "テクノロジー・IT",
            description = "テクノロジーやITに関するタグ",
            imageUrl = "https://picsum.photos/id/6/100/100"
        ),
        MainHomeTagCategoryData(
            id = "7",
            name = "ビジネス・キャリア",
            description = "ビジネスやキャリアに関するタグ",
            imageUrl = "https://picsum.photos/id/7/100/100"
        ),
        MainHomeTagCategoryData(
            id = "8",
            name = "教育・学習",
            description = "教育や学習に関するタグ",
            imageUrl = "https://picsum.photos/id/8/100/100"
        ),
        MainHomeTagCategoryData(
            id = "9",
            name = "健康・ウェルネス",
            description = "健康やウェルネスに関するタグ",
            imageUrl = "https://picsum.photos/id/9/100/100"
        ),
        MainHomeTagCategoryData(
            id = "10",
            name = "アート・クリエイ티브",
            description = "アートやクリエイティブに関するタグ",
            imageUrl = "https://picsum.photos/id/10/100/100"
        )
    )

    fun getUsers(): List<UserDetail> = dummyUserDetails
    
    fun findUserById(id: String): UserDetail? {
        return dummyUserDetails.find { it.id == id }
    }

    fun getHomeTags(): List<MainHomeTag> = dummyHomeTags

    private fun createHomeTags(): List<MainHomeTag> {
        return listOf(
            MainHomeTag("101", "맛집탐방", "전국의 숨겨진 맛집을 찾아 떠나요!", "https://picsum.photos/id/1/200/200", (50..300).random(), "1", TagType.HOBBY),
            MainHomeTag("102", "여행에미치다", "아름다운 풍경과 새로운 문화를 경험해요.", "https://picsum.photos/id/2/200/200", (50..300).random(), "2", TagType.HOBBY),
            MainHomeTag("103", "운동매니아", "함께 땀 흘리며 건강한 삶을 만들어요.", "https://picsum.photos/id/3/200/200", (50..300).random(), "3", TagType.HOBBY),
            MainHomeTag("104", "개발자", "코드로 세상을 바꾸는 사람들", "https://picsum.photos/id/4/200/200", (50..300).random(), "4", TagType.LIFESTYLE),
            MainHomeTag("105", "독서클럽", "책을 통해 지혜와 감동을 나눠요.", "https://picsum.photos/id/5/200/200", (50..300).random(), "5", TagType.HOBBY),
            MainHomeTag("106", "영화광", "주말엔 영화와 함께! 인생 영화 공유해요.", "https://picsum.photos/id/6/200/200", (50..300).random(), "6", TagType.LIFESTYLE),
            MainHomeTag("107", "음악없인못살아", "나만의 플레이리스트를 공유해보세요.", "https://picsum.photos/id/7/200/200", (50..300).random(), "6", TagType.LIFESTYLE),
            MainHomeTag("108", "댕댕이그램", "귀여운 강아지 사진 자랑해요.", "https://picsum.photos/id/8/200/200", (50..300).random(), "7", TagType.LIFESTYLE),
            MainHomeTag("109", "냥집사모임", "고양이의 매력에 빠져보세요.", "https://picsum.photos/id/9/200/200", (50..300).random(), "7", TagType.LIFESTYLE),
            MainHomeTag("110", "패셔니스타", "오늘의 OOTD! 나만의 스타일을 뽐내요.", "https://picsum.photos/id/10/200/200", (50..300).random(), "8", TagType.LIFESTYLE),
            MainHomeTag("111", "요리왕", "나만의 비밀 레시피를 공개합니다.", "https://picsum.photos/id/11/200/200", (50..300).random(), "1", TagType.HOBBY),
            MainHomeTag("112", "캠핑족", "자연 속에서 즐기는 힐링 타임.", "https://picsum.photos/id/12/200/200", (50..300).random(), "2", TagType.HOBBY),
            MainHomeTag("113", "진지한만남", "가벼운 관계보다는 깊이 있는 인연을 찾아요.", "https://picsum.photos/id/20/200/200", (10..100).random(), "10", TagType.VALUE),
            MainHomeTag("114", "긍정적인사람", "밝고 긍정적인 에너지를 가진 분 환영해요.", "https://picsum.photos/id/21/200/200", (10..100).random(), "10", TagType.VALUE),
            MainHomeTag("115", "함께성장", "서로에게 배우고 함께 성장하는 관계를 원해요.", "https://picsum.photos/id/22/200/200", (10..100).random(), "10", TagType.VALUE)
        )
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