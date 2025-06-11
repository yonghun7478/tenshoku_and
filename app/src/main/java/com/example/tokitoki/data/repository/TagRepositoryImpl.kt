package com.example.tokitoki.data.repository

import com.example.tokitoki.data.model.MainHomeTagData
import com.example.tokitoki.domain.model.TagType
import com.example.tokitoki.data.model.toDomain
import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.TagRepository
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
) : TagRepository {

    private val allTags = listOf(
        MainHomeTagData(
            id = "1",
            name = "カフェ巡り",
            description = "お気に入りのカフェを探して、素敵な時間を過ごしましょう。",
            imageUrl = "https://picsum.photos/id/1/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "2",  // 食べ物・飲み物
            tagType = TagType.LIFESTYLE
        ),
        MainHomeTagData(
            id = "2",
            name = "旅行好き",
            description = "新しい場所を発見し、素晴らしい思い出を作りましょう。",
            imageUrl = "https://picsum.photos/id/2/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "3",  // 旅行・観光
            tagType = TagType.HOBBY
        ),
        MainHomeTagData(
            id = "3",
            name = "料理上手",
            description = "美味しい料理を作って、大切な人と共有しましょう。",
            imageUrl = "https://picsum.photos/id/3/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "2",  // 食べ物・飲み物
            tagType = TagType.LIFESTYLE
        ),
        MainHomeTagData(
            id = "4",
            name = "読書家",
            description = "本の世界に浸り、新しい知識と感動を得ましょう。",
            imageUrl = "https://picsum.photos/id/4/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "1",  // 趣味・スポーツ
            tagType = TagType.HOBBY
        ),
        MainHomeTagData(
            id = "5",
            name = "映画好き",
            description = "様々な映画を観て、感動と刺激を受けましょう。",
            imageUrl = "https://picsum.photos/id/5/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "4",  // 音楽・映画
            tagType = TagType.LIFESTYLE
        ),
        MainHomeTagData(
            id = "6",
            name = "音楽好き",
            description = "音楽を通じて、心を癒し、活力を得ましょう。",
            imageUrl = "https://picsum.photos/id/6/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "4",  // 音楽・映画
            tagType = TagType.LIFESTYLE
        ),
        MainHomeTagData(
            id = "7",
            name = "スポーツ好き",
            description = "体を動かして、健康な生活を送りましょう。",
            imageUrl = "https://picsum.photos/id/7/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "1",  // 趣味・スポーツ
            tagType = TagType.HOBBY
        ),
        MainHomeTagData(
            id = "8",
            name = "写真撮影",
            description = "素敵な瞬間を写真に残し、思い出を大切にしましょう。",
            imageUrl = "https://picsum.photos/id/8/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "1",  // 趣味・スポーツ
            tagType = TagType.HOBBY
        ),
        MainHomeTagData(
            id = "9",
            name = "アート好き",
            description = "芸術作品に触れ、感性を磨きましょう。",
            imageUrl = "https://picsum.photos/id/9/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "1",  // 趣味・スポーツ
            tagType = TagType.HOBBY
        ),
        MainHomeTagData(
            id = "10",
            name = "ガーデニング",
            description = "植物を育て、自然と触れ合いましょう。",
            imageUrl = "https://picsum.photos/id/10/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "1",  // 趣味・スポーツ
            tagType = TagType.HOBBY
        ),
        MainHomeTagData(
            id = "11",
            name = "ペット好き",
            description = "かわいいペットと一緒に、幸せな時間を過ごしましょう。",
            imageUrl = "https://picsum.photos/id/11/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "1",  // 趣味・スポーツ
            tagType = TagType.HOBBY
        ),
        MainHomeTagData(
            id = "12",
            name = "ファッション",
            description = "おしゃれを楽しみ、自分らしいスタイルを見つけましょう。",
            imageUrl = "https://picsum.photos/id/12/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "5",  // ファッション・美容
            tagType = TagType.LIFESTYLE
        ),
        MainHomeTagData(
            id = "13",
            name = "ヨガ",
            description = "心と体のバランスを取り、リラックスしましょう。",
            imageUrl = "https://picsum.photos/id/13/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "1",  // 趣味・スポーツ
            tagType = TagType.HOBBY
        ),
        MainHomeTagData(
            id = "14",
            name = "お酒好き",
            description = "美味しいお酒を楽しみ、素敵な時間を過ごしましょう。",
            imageUrl = "https://picsum.photos/id/14/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "2",  // 食べ物・飲み物
            tagType = TagType.LIFESTYLE
        ),
        MainHomeTagData(
            id = "15",
            name = "ゲーム好き",
            description = "ゲームを通じて、新しい世界を体験しましょう。",
            imageUrl = "https://picsum.photos/id/15/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "1",  // 趣味・スポジュ
            tagType = TagType.HOBBY
        ),
        MainHomeTagData(
            id = "16",
            name = "DIY",
            description = "自分で作る喜びを体験しましょう。",
            imageUrl = "https://picsum.photos/id/16/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "1",  // 趣味・スポーツ
            tagType = TagType.HOBBY
        ),
        MainHomeTagData(
            id = "17",
            name = "コーヒー好き",
            description = "美味しいコーヒーで、素敵な時間を過ごしましょう。",
            imageUrl = "https://picsum.photos/id/17/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "2",  // 食べ物・飲み物
            tagType = TagType.LIFESTYLE
        ),
        MainHomeTagData(
            id = "18",
            name = "スイーツ好き",
            description = "甘いものを通じて、幸せな時間を過ごしましょう。",
            imageUrl = "https://picsum.photos/id/18/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "2",  // 食べ物・飲み物
            tagType = TagType.LIFESTYLE
        ),
        MainHomeTagData(
            id = "19",
            name = "温泉好き",
            description = "温泉で心身を癒し、リフレッシュしましょう。",
            imageUrl = "https://picsum.photos/id/19/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "3",  // 旅行・観光
            tagType = TagType.HOBBY
        ),
        MainHomeTagData(
            id = "20",
            name = "登山",
            description = "自然を感じながら、新しい景色を発見しましょう。",
            imageUrl = "https://picsum.photos/id/20/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "3",  // 旅行・観光
            tagType = TagType.HOBBY
        ),
        MainHomeTagData(
            id = "21",
            name = "キャンプ",
            description = "自然の中で、素敵な思い出を作りましょう。",
            imageUrl = "https://picsum.photos/id/21/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "3",  // 旅行・観光
            tagType = TagType.HOBBY
        ),
        MainHomeTagData(
            id = "22",
            name = "釣り",
            description = "海や川で、のんびりとした時間を過ごしましょう。",
            imageUrl = "https://picsum.photos/id/22/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "3",  // 旅行・観光
            tagType = TagType.HOBBY
        ),
        MainHomeTagData(
            id = "23",
            name = "ゴルフ",
            description = "ゴルフを通じて、新しい出会いを楽しみましょう。",
            imageUrl = "https://picsum.photos/id/23/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "3",  // 旅行・観光
            tagType = TagType.HOBBY
        ),
        MainHomeTagData(
            id = "24",
            name = "テニス",
            description = "テニスで体を動かし、爽快感を味わいましょう。",
            imageUrl = "https://picsum.photos/id/24/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "3",  // 旅行・観光
            tagType = TagType.HOBBY
        ),
        MainHomeTagData(
            id = "25",
            name = "ダンス",
            description = "音楽に合わせて、楽しく体を動かしましょう。",
            imageUrl = "https://picsum.photos/id/25/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "4",  // 音楽・映画
            tagType = TagType.LIFESTYLE
        ),
        MainHomeTagData(
            id = "26",
            name = "楽器演奏",
            description = "音楽を奏で、心を豊かにしましょう。",
            imageUrl = "https://picsum.photos/id/26/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "4",  // 音楽・映画
            tagType = TagType.LIFESTYLE
        ),
        MainHomeTagData(
            id = "27",
            name = "絵画",
            description = "絵を描いて、表現する喜びを感じましょう。",
            imageUrl = "https://picsum.photos/id/27/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "4",  // 音楽・映画
            tagType = TagType.LIFESTYLE
        ),
        MainHomeTagData(
            id = "28",
            name = "陶芸",
            description = "土と向き合い, ものづくりの楽しさを体験しましょう。",
            imageUrl = "https://picsum.photos/id/28/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "4",  // 音楽・映画
            tagType = TagType.LIFESTYLE
        ),
        MainHomeTagData(
            id = "29",
            name = "茶道",
            description = "日本の伝統文化에 触れ, 마음을落ち着けましょう。",
            imageUrl = "https://picsum.photos/id/29/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "4",  // 音楽・映画
            tagType = TagType.LIFESTYLE
        ),
        MainHomeTagData(
            id = "30",
            name = "書道",
            description = "文字を書くことで, 마음을整えましょう。",
            imageUrl = "https://picsum.photos/id/30/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "4",  // 音楽・映画
            tagType = TagType.LIFESTYLE
        ),
        MainHomeTagData(
            id = "31",
            name = "정직",
            description = "정직한 삶의 가치를 중요하게 생각하는 사람들과 함께하세요.",
            imageUrl = "https://picsum.photos/id/31/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "6",  // 가치관 (새로운 카테고리 ID)
            tagType = TagType.VALUE
        ),
        MainHomeTagData(
            id = "32",
            name = "책임감",
            description = "자신의 역할과 의무를 다하는 책임감 있는 사람들과 함께하세요.",
            imageUrl = "https://picsum.photos/id/32/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "6",
            tagType = TagType.VALUE
        ),
        MainHomeTagData(
            id = "33",
            name = "도전정신",
            description = "새로운 것에 도전하고 성장을 추구하는 사람들과 함께하세요.",
            imageUrl = "https://picsum.photos/id/33/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "6",
            tagType = TagType.VALUE
        ),
        MainHomeTagData(
            id = "34",
            name = "성실함",
            description = "꾸준하고 성실하게 노력하는 사람들과 함께하세요.",
            imageUrl = "https://picsum.photos/id/34/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "6",
            tagType = TagType.VALUE
        ),
        MainHomeTagData(
            id = "35",
            name = "배려심",
            description = "타인을 존중하고 배려하는 따뜻한 마음을 가진 사람들과 함께하세요.",
            imageUrl = "https://picsum.photos/id/35/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "6",
            tagType = TagType.VALUE
        ),
        MainHomeTagData(
            id = "36",
            name = "긍정적",
            description = "어떤 상황에서도 긍정적인 에너지를 잃지 않는 사람들과 함께하세요.",
            imageUrl = "https://picsum.photos/id/36/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "6",
            tagType = TagType.VALUE
        ),
        MainHomeTagData(
            id = "37",
            name = "신뢰",
            description = "서로에게 신뢰를 기반으로 하는 관계를 중요하게 생각하는 사람들과 함께하세요.",
            imageUrl = "https://picsum.photos/id/37/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "6",
            tagType = TagType.VALUE
        ),
        MainHomeTagData(
            id = "38",
            name = "공정함",
            description = "모든 일에 공정함을 추구하며 정의로운 사람들과 함께하세요.",
            imageUrl = "https://picsum.photos/id/38/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "6",
            tagType = TagType.VALUE
        ),
        MainHomeTagData(
            id = "39",
            name = "성장",
            description = "개인의 성장을 중요하게 생각하고 끊임없이 배우는 사람들과 함께하세요.",
            imageUrl = "https://picsum.photos/id/39/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "6",
            tagType = TagType.VALUE
        ),
        MainHomeTagData(
            id = "40",
            name = "겸손",
            description = "항상 겸손한 태도로 배우고 소통하는 사람들과 함께하세요.",
            imageUrl = "https://picsum.photos/id/40/100/100",
            subscriberCount = (50..300).random(),
            categoryId = "6",
            tagType = TagType.VALUE
        )
    )

    override suspend fun getTagTypeList(): List<TagType> {
        return TagType.entries.toList()
    }

    override suspend fun getTagsByType(tagType: TagType): Result<List<MainHomeTag>> {
        val dummyTags = allTags.filter { it.tagType == tagType }
        return Result.success(dummyTags.map { it.toDomain() })
    }

    override suspend fun getTags(tagIds: List<Int>): List<MainHomeTag> {
        return allTags.filter { tagIds.contains(it.id.toIntOrNull()) }.map { it.toDomain() }
    }

    override suspend fun getAllTags(): Result<List<MainHomeTag>> {
        return Result.success(allTags.map { it.toDomain() })
    }
}