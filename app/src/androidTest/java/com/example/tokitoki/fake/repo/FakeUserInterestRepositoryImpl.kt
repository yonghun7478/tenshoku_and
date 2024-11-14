package com.example.tokitoki.fake.repo

import com.example.tokitoki.data.local.CategoryEntity
import com.example.tokitoki.data.local.UserInterestDao
import com.example.tokitoki.data.local.UserInterestWithCategoryEntity
import com.example.tokitoki.domain.converter.CategoryConverter
import com.example.tokitoki.domain.converter.UserInterestConverter
import com.example.tokitoki.domain.model.Category
import com.example.tokitoki.domain.model.UserInterest
import com.example.tokitoki.domain.repository.UserInterestRepository
import javax.inject.Inject

class FakeUserInterestRepositoryImpl @Inject constructor(
    private val userInterestDao: UserInterestDao
) : UserInterestRepository {
    override suspend fun getCategories(): List<Category> {
        val categoryEntities = listOf(
            CategoryEntity(id = 1, title = "趣味"),          // "Hobby"
            CategoryEntity(id = 2, title = "ライフスタイル"),  // "Lifestyle"
            CategoryEntity(id = 3, title = "価値観")          // "Values"
        )
        return categoryEntities.map { CategoryConverter.dataToDomain(it) }
    }

    override suspend fun getUserInterests(categoryId: Int): List<UserInterest> {
        var userInterestEntities = emptyList<UserInterestWithCategoryEntity>()
        when (categoryId) {
            1 -> {
                userInterestEntities = listOf(
                    // 趣味 (Hobby) 카테고리
                    UserInterestWithCategoryEntity(
                        interestId = 1,
                        interestTitle = "ヨガ1",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 2,
                        interestTitle = "ヨガ2",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 3,
                        interestTitle = "ヨガ3",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 4,
                        interestTitle = "Hobby Crafting",
                        interestUrl = "https://example.com/hobby4",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 5,
                        interestTitle = "Hobby Gaming",
                        interestUrl = "https://example.com/hobby5",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 6,
                        interestTitle = "ヨガ4",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 7,
                        interestTitle = "ヨガ5",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 8,
                        interestTitle = "ヨガ6",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 9,
                        interestTitle = "Hobby Crafting",
                        interestUrl = "https://example.com/hobby4",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 10,
                        interestTitle = "Hobby Gaming",
                        interestUrl = "https://example.com/hobby5",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 11,
                        interestTitle = "ヨガ7",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 12,
                        interestTitle = "ヨガ8",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 13,
                        interestTitle = "ヨガ9",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 14,
                        interestTitle = "Hobby Crafting",
                        interestUrl = "https://example.com/hobby4",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 15,
                        interestTitle = "Hobby Gaming",
                        interestUrl = "https://example.com/hobby5",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 16,
                        interestTitle = "ヨガ10",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 17,
                        interestTitle = "ヨガ11",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 18,
                        interestTitle = "ヨガ12",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 19,
                        interestTitle = "Hobby Crafting",
                        interestUrl = "https://example.com/hobby4",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 20,
                        interestTitle = "Hobby Gaming",
                        interestUrl = "https://example.com/hobby5",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 21,
                        interestTitle = "ヨガ13",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 22,
                        interestTitle = "ヨガ14",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 23,
                        interestTitle = "ヨガ15",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 24,
                        interestTitle = "Hobby Crafting",
                        interestUrl = "https://example.com/hobby4",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 25,
                        interestTitle = "Hobby Gaming",
                        interestUrl = "https://example.com/hobby5",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 26,
                        interestTitle = "ヨガ16",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 27,
                        interestTitle = "ヨガ17",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 28,
                        interestTitle = "ヨガ18",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 29,
                        interestTitle = "Hobby Crafting",
                        interestUrl = "https://example.com/hobby4",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 30,
                        interestTitle = "Hobby Gaming",
                        interestUrl = "https://example.com/hobby5",
                        categoryId = 1,
                        categoryTitle = "趣味"
                    ),
                )
            }

            2 -> {
                userInterestEntities = listOf(
                    // 趣味 (Hobby) 카테고리
                    UserInterestWithCategoryEntity(
                        interestId = 1,
                        interestTitle = "ヨガ1",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 2,
                        categoryTitle = "ライフスタイル"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 2,
                        interestTitle = "ヨガ2",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 2,
                        categoryTitle = "ライフスタイル"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 3,
                        interestTitle = "ヨガ3",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 2,
                        categoryTitle = "ライフスタイル"
                    ),
                )
            }

            else -> {
                userInterestEntities = listOf(
                    // 趣味 (Hobby) 카테고리
                    UserInterestWithCategoryEntity(
                        interestId = 1,
                        interestTitle = "ヨガ1",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 3,
                        categoryTitle = "価値観"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 2,
                        interestTitle = "ヨガ2",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 3,
                        categoryTitle = "価値観"
                    ),
                    UserInterestWithCategoryEntity(
                        interestId = 3,
                        interestTitle = "ヨガ3",
                        interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                        categoryId = 3,
                        categoryTitle = "価値観"
                    ),
                )
            }
        }

        return userInterestEntities.map { UserInterestConverter.dataToDomain(it) }
    }
}