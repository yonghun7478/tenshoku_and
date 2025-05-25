package com.example.tokitoki.data.repository

import com.example.tokitoki.data.model.MainHomeTagData
import com.example.tokitoki.data.model.MainHomeTagDetailData
import com.example.tokitoki.data.model.MainHomeTagSubscriberData
import com.example.tokitoki.data.model.toDomain
import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.model.MainHomeTagDetail
import com.example.tokitoki.domain.model.MainHomeTagSubscriber
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MainHomeTagRepositoryImpl @Inject constructor() : MainHomeTagRepository {

    // 전체 태그 데이터 정의 (30개)
    private val allTags = listOf(
        MainHomeTagData(
            id = "1",
            name = "カフェ巡り",
            description = "お気に入りのカフェを探して、素敵な時間を過ごしましょう。",
            imageUrl = "https://picsum.photos/id/1/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "2",
            name = "旅行好き",
            description = "新しい場所を発見し、素晴らしい思い出を作りましょう。",
            imageUrl = "https://picsum.photos/id/2/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "3",
            name = "料理上手",
            description = "美味しい料理を作って、大切な人と共有しましょう。",
            imageUrl = "https://picsum.photos/id/3/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "4",
            name = "読書家",
            description = "本の世界に浸り、新しい知識と感動を得ましょう。",
            imageUrl = "https://picsum.photos/id/4/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "5",
            name = "映画好き",
            description = "様々な映画を観て、感動と刺激を受けましょう。",
            imageUrl = "https://picsum.photos/id/5/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "6",
            name = "音楽好き",
            description = "音楽を通じて、心を癒し、活力を得ましょう。",
            imageUrl = "https://picsum.photos/id/6/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "7",
            name = "スポーツ好き",
            description = "体を動かして、健康な生活を送りましょう。",
            imageUrl = "https://picsum.photos/id/7/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "8",
            name = "写真撮影",
            description = "素敵な瞬間を写真に残し、思い出を大切にしましょう。",
            imageUrl = "https://picsum.photos/id/8/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "9",
            name = "アート好き",
            description = "芸術作品に触れ、感性を磨きましょう。",
            imageUrl = "https://picsum.photos/id/9/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "10",
            name = "ガーデニング",
            description = "植物を育て、自然と触れ合いましょう。",
            imageUrl = "https://picsum.photos/id/10/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "11",
            name = "ペット好き",
            description = "かわいいペットと一緒に、幸せな時間を過ごしましょう。",
            imageUrl = "https://picsum.photos/id/11/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "12",
            name = "ファッション",
            description = "おしゃれを楽しみ、自分らしいスタイルを見つけましょう。",
            imageUrl = "https://picsum.photos/id/12/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "13",
            name = "ヨガ",
            description = "心と体のバランスを取り、リラックスしましょう。",
            imageUrl = "https://picsum.photos/id/13/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "14",
            name = "お酒好き",
            description = "美味しいお酒を楽しみ、素敵な時間を過ごしましょう。",
            imageUrl = "https://picsum.photos/id/14/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "15",
            name = "ゲーム好き",
            description = "ゲームを通じて、新しい世界を体験しましょう。",
            imageUrl = "https://picsum.photos/id/15/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "16",
            name = "DIY",
            description = "自分で作る喜びを体験しましょう。",
            imageUrl = "https://picsum.photos/id/16/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "17",
            name = "コーヒー好き",
            description = "美味しいコーヒーで、素敵な時間を過ごしましょう。",
            imageUrl = "https://picsum.photos/id/17/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "18",
            name = "スイーツ好き",
            description = "甘いものを通じて、幸せな時間を過ごしましょう。",
            imageUrl = "https://picsum.photos/id/18/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "19",
            name = "温泉好き",
            description = "温泉で心身を癒し、リフレッシュしましょう。",
            imageUrl = "https://picsum.photos/id/19/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "20",
            name = "登山",
            description = "自然を感じながら、新しい景色を発見しましょう。",
            imageUrl = "https://picsum.photos/id/20/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "21",
            name = "キャンプ",
            description = "自然の中で、素敵な思い出を作りましょう。",
            imageUrl = "https://picsum.photos/id/21/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "22",
            name = "釣り",
            description = "海や川で、のんびりとした時間を過ごしましょう。",
            imageUrl = "https://picsum.photos/id/22/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "23",
            name = "ゴルフ",
            description = "ゴルフを通じて、新しい出会いを楽しみましょう。",
            imageUrl = "https://picsum.photos/id/23/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "24",
            name = "テニス",
            description = "テニスで体を動かし、爽快感を味わいましょう。",
            imageUrl = "https://picsum.photos/id/24/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "25",
            name = "ダンス",
            description = "音楽に合わせて、楽しく体を動かしましょう。",
            imageUrl = "https://picsum.photos/id/25/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "26",
            name = "楽器演奏",
            description = "音楽を奏で、心を豊かにしましょう。",
            imageUrl = "https://picsum.photos/id/26/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "27",
            name = "絵画",
            description = "絵を描いて、表現する喜びを感じましょう。",
            imageUrl = "https://picsum.photos/id/27/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "28",
            name = "陶芸",
            description = "土と向き合い、ものづくりの楽しさを体験しましょう。",
            imageUrl = "https://picsum.photos/id/28/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "29",
            name = "茶道",
            description = "日本の伝統文化に触れ、心を落ち着けましょう。",
            imageUrl = "https://picsum.photos/id/29/100/100",
            subscriberCount = (50..300).random()
        ),
        MainHomeTagData(
            id = "30",
            name = "書道",
            description = "文字を書くことで、心を整えましょう。",
            imageUrl = "https://picsum.photos/id/30/100/100",
            subscriberCount = (50..300).random()
        )
    )

    // 각 섹션별 태그 할당
    private val todayTag = allTags[0].toDomain() // カフェ巡り

    private val trendingTags = listOf(
        allTags[1],  // 旅行好き
        allTags[2],  // 料理上手
        allTags[3],  // 読書家
        allTags[4],  // 映画好き
        allTags[5],  // 音楽好き
        allTags[6]   // スポーツ好き
    ).map { it.toDomain() }

    private val myTags = listOf(
        allTags[7],   // 写真撮影
        allTags[8],   // アート好き
        allTags[9],   // ガーデニング
        allTags[10],  // ペット好き
        allTags[11]   // ファッション
    ).map { it.toDomain() }

    private val suggestedTags = listOf(
        allTags[12], // ヨガ
        allTags[13], // お酒好き
        allTags[14], // ゲーム好き
        allTags[15], // DIY
        allTags[16], // コーヒー好き
        allTags[17]  // スイーツ好き
    ).map { it.toDomain() }

    // 태그 상세 정보
    private val tagDetails = allTags.associate { tag ->
        tag.id to MainHomeTagDetailData(
            id = tag.id,
            name = tag.name,
            description = tag.description,
            imageUrl = tag.imageUrl,
            subscriberCount = tag.subscriberCount
        )
    }

    // 태그 구독자 정보 (UserRepositoryImpl의 유저 데이터 참고)
    private val tagSubscribers = mapOf(
        "1" to listOf(
            MainHomeTagSubscriberData(
                userId = "1",
                profileImageUrl = "https://cdn.mhnse.com/news/photo/202411/354069_409655_3737.jpg",
                age = 25,
                location = "서울시 강남구"
            ),
            MainHomeTagSubscriberData(
                userId = "2",
                profileImageUrl = "https://img.hankyung.com/photo/202411/03.38739677.1.jpg",
                age = 30,
                location = "서울시 서초구"
            ),
            MainHomeTagSubscriberData(
                userId = "3",
                profileImageUrl = "https://image.newdaily.co.kr/site/data/img/2025/02/03/2025020300300_0.jpg",
                age = 28,
                location = "서울시 송파구"
            )
        ),
        "2" to listOf(
            MainHomeTagSubscriberData(
                userId = "4",
                profileImageUrl = "https://cdn.hankooki.com/news/photo/202504/247606_344901_1744771524.jpg",
                age = 32,
                location = "서울시 마포구"
            ),
            MainHomeTagSubscriberData(
                userId = "5",
                profileImageUrl = "https://talkimg.imbc.com/TVianUpload/tvian/TViews/image/2025/02/03/140455b2-3135-4315-9ca1-9c68a7db7546.jpg",
                age = 27,
                location = "서울시 용산구"
            ),
            MainHomeTagSubscriberData(
                userId = "6",
                profileImageUrl = "https://www.cosinkorea.com/data/photos/20240414/art_17120529936565_2a8f34.jpg",
                age = 29,
                location = "서울시 종로구"
            )
        ),
        "3" to listOf(
            MainHomeTagSubscriberData(
                userId = "7",
                profileImageUrl = "https://cdn.hankooki.com/news/photo/202503/243191_338487_1743185951.jpg",
                age = 31,
                location = "서울시 중구"
            ),
            MainHomeTagSubscriberData(
                userId = "8",
                profileImageUrl = "https://img.hankyung.com/photo/202504/03.39959436.1.jpg",
                age = 26,
                location = "서울시 동작구"
            )
        ),
        "4" to listOf(
            MainHomeTagSubscriberData(
                userId = "9",
                profileImageUrl = "https://cdn.mhnse.com/news/photo/202411/354069_409655_3737.jpg",
                age = 33,
                location = "서울시 서대문구"
            ),
            MainHomeTagSubscriberData(
                userId = "10",
                profileImageUrl = "https://img.hankyung.com/photo/202411/03.38739677.1.jpg",
                age = 28,
                location = "서울시 은평구"
            )
        ),
        "5" to listOf(
            MainHomeTagSubscriberData(
                userId = "11",
                profileImageUrl = "https://image.newdaily.co.kr/site/data/img/2025/02/03/2025020300300_0.jpg",
                age = 29,
                location = "서울시 노원구"
            ),
            MainHomeTagSubscriberData(
                userId = "12",
                profileImageUrl = "https://cdn.hankooki.com/news/photo/202504/247606_344901_1744771524.jpg",
                age = 31,
                location = "서울시 도봉구"
            )
        ),
        "6" to listOf(
            MainHomeTagSubscriberData(
                userId = "13",
                profileImageUrl = "https://talkimg.imbc.com/TVianUpload/tvian/TViews/image/2025/02/03/140455b2-3135-4315-9ca1-9c68a7db7546.jpg",
                age = 27,
                location = "서울시 강동구"
            ),
            MainHomeTagSubscriberData(
                userId = "14",
                profileImageUrl = "https://www.cosinkorea.com/data/photos/20240414/art_17120529936565_2a8f34.jpg",
                age = 30,
                location = "서울시 광진구"
            )
        ),
        "7" to listOf(
            MainHomeTagSubscriberData(
                userId = "15",
                profileImageUrl = "https://cdn.hankooki.com/news/photo/202503/243191_338487_1743185951.jpg",
                age = 32,
                location = "서울시 구로구"
            ),
            MainHomeTagSubscriberData(
                userId = "16",
                profileImageUrl = "https://img.hankyung.com/photo/202504/03.39959436.1.jpg",
                age = 28,
                location = "서울시 금천구"
            )
        ),
        "8" to listOf(
            MainHomeTagSubscriberData(
                userId = "17",
                profileImageUrl = "https://cdn.mhnse.com/news/photo/202411/354069_409655_3737.jpg",
                age = 29,
                location = "서울시 강서구"
            ),
            MainHomeTagSubscriberData(
                userId = "18",
                profileImageUrl = "https://img.hankyung.com/photo/202411/03.38739677.1.jpg",
                age = 31,
                location = "서울시 양천구"
            )
        )
    )

    private var recentSearches: List<MainHomeTagData> = listOf()
    private var selectedTags: MutableList<MainHomeTag> = mutableListOf()
    private var tempSelectedTags: List<MainHomeTag> = listOf()

    override suspend fun getTodayTag(): Result<MainHomeTag> {
        delay(500)
        return Result.success(todayTag)
    }

    override suspend fun getTrendingTags(): Result<List<MainHomeTag>> {
        delay(700)
        return Result.success(trendingTags)
    }

    override suspend fun getMyTags(): Result<List<MainHomeTag>> {
        delay(300)
        return Result.success(myTags)
    }

    override suspend fun getSuggestedTags(): Result<List<MainHomeTag>> {
        delay(400)
        return Result.success(suggestedTags)
    }
}