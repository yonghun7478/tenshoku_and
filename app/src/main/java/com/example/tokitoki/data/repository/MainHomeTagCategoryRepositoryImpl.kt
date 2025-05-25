package com.example.tokitoki.data.repository

import com.example.tokitoki.data.model.MainHomeTagCategoryData
import com.example.tokitoki.domain.model.MainHomeTagCategory
import com.example.tokitoki.domain.repository.MainHomeTagCategoryRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MainHomeTagCategoryRepositoryImpl @Inject constructor() : MainHomeTagCategoryRepository {

    // 임시 카테고리 데이터 정의
    private val categories = listOf(
        MainHomeTagCategoryData(
            id = "1",
            name = "趣味・スポーツ",
            description = "趣味やスポーツに関するタグ",
            imageUrl = "https://picsum.photos/id/1/100/100"
        ),
        MainHomeTagCategoryData(
            id = "2",
            name = "食べ物・飲み物",
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
            name = "アート・クリエイティブ",
            description = "アートやクリエイティブに関するタグ",
            imageUrl = "https://picsum.photos/id/10/100/100"
        )
    )

    override suspend fun getCategories(): Result<List<MainHomeTagCategory>> {
        return try {
            // API 호출을 시뮬레이션하기 위한 딜레이
            delay(1000)
            Result.success(categories.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 