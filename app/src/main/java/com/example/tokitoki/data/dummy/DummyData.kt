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
            name = "趣味・エンタメ", // Hobbies & Entertainment
            description = "趣味やエンタメに関するタグ",
            imageUrl = "https://picsum.photos/id/1/100/100"
        ),
        MainHomeTagCategoryData(
            id = "2",
            name = "食・グルメ", // Food & Gourmet
            description = "食べ物や飲み物に関するタグ",
            imageUrl = "https://picsum.photos/id/2/100/100"
        ),
        MainHomeTagCategoryData(
            id = "3",
            name = "旅行・お出かけ", // Travel & Outings
            description = "旅行や観光、お出かけに関するタグ",
            imageUrl = "https://picsum.photos/id/3/100/100"
        ),
        MainHomeTagCategoryData(
            id = "4",
            name = "ライフスタイル", // Lifestyle
            description = "日々の生活や価値観に関するタグ",
            imageUrl = "https://picsum.photos/id/4/100/100"
        ),
        MainHomeTagCategoryData(
            id = "5",
            name = "学び・スキル", // Learning & Skills
            description = "自己成長やスキルアップに関するタグ",
            imageUrl = "https://picsum.photos/id/5/100/100"
        )
    )

    fun getUsers(): List<UserDetail> = dummyUserDetails
    
    fun findUserById(id: String): UserDetail? {
        return dummyUserDetails.find { it.id == id }
    }

    fun getHomeTags(): List<MainHomeTag> = dummyHomeTags

    private fun createHomeTags(): List<MainHomeTag> {
        return listOf(
            MainHomeTag("101", "映画鑑賞", "週末は映画！人生最高の映画をシェアしましょう。", "https://picsum.photos/id/1/200/200", (50..300).random(), "1", TagType.HOBBY),
            MainHomeTag("102", "音楽鑑賞", "自分だけのプレイリストを共有してみませんか？", "https://picsum.photos/id/7/200/200", (50..300).random(), "1", TagType.HOBBY),
            MainHomeTag("103", "読書クラブ", "本を通じて知恵と感動を分かち合いましょう。", "https://picsum.photos/id/5/200/200", (50..300).random(), "1", TagType.HOBBY),
            MainHomeTag("104", "ゲーム仲間", "一緒にゲームを楽しみましょう！", "https://picsum.photos/id/6/200/200", (50..300).random(), "1", TagType.HOBBY),
            MainHomeTag("105", "アート・ものづくり", "クリエイティブな活動を一緒に楽しみませんか？", "https://picsum.photos/id/10/200/200", (50..300).random(), "1", TagType.HOBBY),

            MainHomeTag("106", "カフェ巡り", "隠れた名店カフェを探しに出かけましょう！", "https://picsum.photos/id/11/200/200", (50..300).random(), "2", TagType.HOBBY),
            MainHomeTag("107", "料理・スイーツ", "自分だけの秘密のレシピを公開します！", "https://picsum.photos/id/11/200/200", (50..300).random(), "2", TagType.HOBBY),
            MainHomeTag("108", "食べログ巡り", "全国の隠れた名店を探しに出かけましょう！", "https://picsum.photos/id/1/200/200", (50..300).random(), "2", TagType.HOBBY),

            MainHomeTag("109", "国内旅行", "日本の美しい風景や文化を体験しましょう。", "https://picsum.photos/id/2/200/200", (50..300).random(), "3", TagType.HOBBY),
            MainHomeTag("110", "海外旅行", "世界の新しい文化を体験しましょう。", "https://picsum.photos/id/12/200/200", (50..300).random(), "3", TagType.HOBBY),
            MainHomeTag("111", "アウトドア・キャンプ", "自然の中で楽しむ癒しの時間。", "https://picsum.photos/id/12/200/200", (50..300).random(), "3", TagType.HOBBY),

            MainHomeTag("112", "ペット愛", "可愛いペットの写真を自慢しましょう。", "https://picsum.photos/id/8/200/200", (50..300).random(), "4", TagType.LIFESTYLE),
            MainHomeTag("113", "美容・ファッション", "今日のOOTD！自分だけのスタイルをアピールしましょう。", "https://picsum.photos/id/10/200/200", (50..300).random(), "4", TagType.LIFESTYLE),
            MainHomeTag("114", "健康・フィットネス", "一緒に汗を流して健康な生活を送りましょう。", "https://picsum.photos/id/3/200/200", (50..300).random(), "4", TagType.LIFESTYLE),

            MainHomeTag("115", "プログラミング", "コードで世界を変える人々。", "https://picsum.photos/id/4/200/200", (50..300).random(), "5", TagType.LIFESTYLE),
            MainHomeTag("116", "語学学習", "新しい言語を一緒に学びませんか？", "https://picsum.photos/id/9/200/200", (50..300).random(), "5", TagType.LIFESTYLE),
            MainHomeTag("117", "自己啓発", "お互いに学び、共に成長する関係を求めています。", "https://picsum.photos/id/22/200/200", (10..100).random(), "5", TagType.VALUE)
        )
    }

    private fun createUserDetail(userId: String): UserDetail {
        val locations = listOf("東京", "大阪", "名古屋", "福岡", "札幌", "横浜", "京都")
        val introductions = listOf(
            "はじめまして！良い時間を一緒に過ごしましょう。",
            "新しい出会いを探しています。よろしくお願いします。",
            "こんにちは、プロフィールを読んでいただきありがとうございます！お話してみませんか？",
            "美味しいものを食べに行ったり、面白い映画を観るのが好きです。",
            "運動が好きで、新しい経験を楽しむタイプです。"
        )
        val bloodTypes = listOf("A型", "B型", "O型", "AB型", "その他")
        val educations = listOf("高校卒業", "大学在学中", "大学卒業", "大学院在学中", "修士課程修了", "博士課程")
        val occupations = listOf("会社員", "学生", "フリーランス", "自営業", "エンジニア", "デザイナー", "医療関係", "教育関係")
        val appearances = listOf("清潔感のあるスタイル", "カジュアル好き", "ダンディなスタイル", "個性的なタイプ", "リラックススタイル")
        val datingPhilosophies = listOf("真剣な出会いを求める", "良い友達から始めたい", "お互いを知る過程が大切", "共に成長する恋愛")
        val marriageViews = listOf("結婚に前向き", "まだよく分かりません", "良いご縁があれば考えたい", "結婚は考えていません")
        
        val allPersonalityTraits = listOf("活発な", "落ち着いた", "ユーモラスな", "真面目な", "ポジティブな", "現実的な", "社交的な", "内向的な", "繊細な", "おおらかな")
        val allHobbies = listOf("映画鑑賞", "音楽鑑賞", "読書", "運動", "旅行", "料理", "ゲーム", "散歩", "写真撮影", "楽器演奏")
        val allMyTags = listOf("#日常", "#食べログ", "#旅行好き", "#エンジニア", "#運動", "#読書", "#映画", "#音楽", "#犬好き", "#猫好き", "#プログラミング", "#週末の過ごし方")
        val lifestyles = listOf("規則正しい生活を大切にしています。", "自由な魂です！", "主に家で過ごします。", "新しいことを学ぶのが好きです。", "旅行でエネルギーを得ます。")

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