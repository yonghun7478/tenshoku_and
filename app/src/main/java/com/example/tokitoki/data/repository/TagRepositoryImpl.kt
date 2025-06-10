package com.example.tokitoki.data.repository

import com.example.tokitoki.data.model.MainHomeTagData
import com.example.tokitoki.data.model.TagType
import com.example.tokitoki.data.model.toDomain
import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.TagRepository
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
) : TagRepository {

    private val allTags = listOf(
        MainHomeTagData(
            id = "hobby_1",
            name = "요가",
            description = "몸과 마음을 수련하는 시간",
            imageUrl = "https://picsum.photos/id/10/100/100",
            subscriberCount = (100..500).random(),
            categoryId = "1",
            tagType = TagType.HOBBY
        ),
        MainHomeTagData(
            id = "hobby_2",
            name = "독서 모임",
            description = "함께 책 읽고 토론해요",
            imageUrl = "https://picsum.photos/id/20/100/100",
            subscriberCount = (100..500).random(),
            categoryId = "1",
            tagType = TagType.HOBBY
        ),
        MainHomeTagData(
            id = "hobby_3",
            name = "사진 찍기",
            description = "일상의 아름다움을 담아요",
            imageUrl = "https://picsum.photos/id/30/100/100",
            subscriberCount = (100..500).random(),
            categoryId = "1",
            tagType = TagType.HOBBY
        ),
        MainHomeTagData(
            id = "lifestyle_1",
            name = "미니멀 라이프",
            description = "간소하게 사는 삶의 지혜",
            imageUrl = "https://picsum.photos/id/40/100/100",
            subscriberCount = (100..500).random(),
            categoryId = "2",
            tagType = TagType.LIFESTYLE
        ),
        MainHomeTagData(
            id = "lifestyle_2",
            name = "건강한 식단",
            description = "몸에 좋은 음식으로 활력 충전",
            imageUrl = "https://picsum.photos/id/50/100/100",
            subscriberCount = (100..500).random(),
            categoryId = "2",
            tagType = TagType.LIFESTYLE
        ),
        MainHomeTagData(
            id = "lifestyle_3",
            name = "친환경 생활",
            description = "지구를 위한 작은 실천",
            imageUrl = "https://picsum.photos/id/60/100/100",
            subscriberCount = (100..500).random(),
            categoryId = "2",
            tagType = TagType.LIFESTYLE
        ),
        MainHomeTagData(
            id = "value_1",
            name = "성장",
            description = "끊임없이 배우고 발전하는 삶",
            imageUrl = "https://picsum.photos/id/70/100/100",
            subscriberCount = (100..500).random(),
            categoryId = "3",
            tagType = TagType.VALUE
        ),
        MainHomeTagData(
            id = "value_2",
            name = "긍정",
            description = "밝고 긍정적인 생각으로 행복 찾기",
            imageUrl = "https://picsum.photos/id/80/100/100",
            subscriberCount = (100..500).random(),
            categoryId = "3",
            tagType = TagType.VALUE
        ),
        MainHomeTagData(
            id = "value_3",
            name = "도전",
            description = "새로운 것에 과감히 도전하는 용기",
            imageUrl = "https://picsum.photos/id/90/100/100",
            subscriberCount = (100..500).random(),
            categoryId = "3",
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
}