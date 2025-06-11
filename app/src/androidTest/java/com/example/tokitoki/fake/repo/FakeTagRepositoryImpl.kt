package com.example.tokitoki.fake.repo

import com.example.tokitoki.data.local.CategoryEntity
import com.example.tokitoki.data.local.TagDao
import com.example.tokitoki.data.local.TagWithCategoryEntity
import com.example.tokitoki.domain.converter.TagConverter
import com.example.tokitoki.domain.model.Tag
import com.example.tokitoki.domain.repository.TagRepository
import javax.inject.Inject

//class FakeTagRepositoryImpl @Inject constructor(
//    private val tagDao: TagDao
//) : TagRepository {
//    override suspend fun getCategories(): List<Category> {
//        val categoryEntities = listOf(
//            CategoryEntity(id = 1, title = "趣味"),          // "Hobby"
//            CategoryEntity(id = 2, title = "ライフスタイル"),  // "Lifestyle"
//            CategoryEntity(id = 3, title = "価値観")          // "Values"
//        )
//        return categoryEntities.map { CategoryConverter.dataToDomain(it) }
//    }
//
//    override suspend fun getTags(categoryId: Int): List<Tag> {
//        var tagEntities = emptyList<TagWithCategoryEntity>()
//        when (categoryId) {
//            1 -> {
//                tagEntities = listOf(
//                    // 趣味 (Hobby) 카테고리
//                    TagWithCategoryEntity(
//                        tagId = 1,
//                        tagTitle = "ヨガ1",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 2,
//                        tagTitle = "ヨガ2",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 3,
//                        tagTitle = "ヨガ3",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 4,
//                        tagTitle = "Hobby Crafting",
//                        tagUrl = "https://example.com/hobby4",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 5,
//                        tagTitle = "Hobby Gaming",
//                        tagUrl = "https://example.com/hobby5",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 6,
//                        tagTitle = "ヨガ4",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 7,
//                        tagTitle = "ヨガ5",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 8,
//                        tagTitle = "ヨガ6",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 9,
//                        tagTitle = "Hobby Crafting",
//                        tagUrl = "https://example.com/hobby4",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 10,
//                        tagTitle = "Hobby Gaming",
//                        tagUrl = "https://example.com/hobby5",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 11,
//                        tagTitle = "ヨガ7",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 12,
//                        tagTitle = "ヨガ8",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 13,
//                        tagTitle = "ヨガ9",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 14,
//                        tagTitle = "Hobby Crafting",
//                        tagUrl = "https://example.com/hobby4",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 15,
//                        tagTitle = "Hobby Gaming",
//                        tagUrl = "https://example.com/hobby5",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 16,
//                        tagTitle = "ヨガ10",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 17,
//                        tagTitle = "ヨガ11",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 18,
//                        tagTitle = "ヨガ12",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 19,
//                        tagTitle = "Hobby Crafting",
//                        tagUrl = "https://example.com/hobby4",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 20,
//                        tagTitle = "Hobby Gaming",
//                        tagUrl = "https://example.com/hobby5",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 21,
//                        tagTitle = "ヨガ13",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 22,
//                        tagTitle = "ヨガ14",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 23,
//                        tagTitle = "ヨガ15",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 24,
//                        tagTitle = "Hobby Crafting",
//                        tagUrl = "https://example.com/hobby4",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 25,
//                        tagTitle = "Hobby Gaming",
//                        tagUrl = "https://example.com/hobby5",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 26,
//                        tagTitle = "ヨガ16",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 27,
//                        tagTitle = "ヨガ17",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 28,
//                        tagTitle = "ヨガ18",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 29,
//                        tagTitle = "Hobby Crafting",
//                        tagUrl = "https://example.com/hobby4",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 30,
//                        tagTitle = "Hobby Gaming",
//                        tagUrl = "https://example.com/hobby5",
//                        categoryId = 1,
//                        categoryTitle = "趣味"
//                    ),
//                )
//            }
//
//            2 -> {
//                tagEntities = listOf(
//                    // 趣味 (Hobby) 카테고리
//                    TagWithCategoryEntity(
//                        tagId = 1,
//                        tagTitle = "ヨガ1",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 2,
//                        categoryTitle = "ライフスタイル"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 2,
//                        tagTitle = "ヨガ2",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 2,
//                        categoryTitle = "ライフスタイル"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 3,
//                        tagTitle = "ヨガ3",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 2,
//                        categoryTitle = "ライフスタイル"
//                    ),
//                )
//            }
//
//            else -> {
//                tagEntities = listOf(
//                    // 趣味 (Hobby) 카테고리
//                    TagWithCategoryEntity(
//                        tagId = 1,
//                        tagTitle = "ヨガ1",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 3,
//                        categoryTitle = "価値観"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 2,
//                        tagTitle = "ヨガ2",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 3,
//                        categoryTitle = "価値観"
//                    ),
//                    TagWithCategoryEntity(
//                        tagId = 3,
//                        tagTitle = "ヨガ3",
//                        tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
//                        categoryId = 3,
//                        categoryTitle = "価値観"
//                    ),
//                )
//            }
//        }
//
//        return tagEntities.map { TagConverter.dataToDomain(it) }
//    }
//
//    override suspend fun getTags(tagIds: List<Int>): List<Tag> {
//        return emptyList()
//    }
//}