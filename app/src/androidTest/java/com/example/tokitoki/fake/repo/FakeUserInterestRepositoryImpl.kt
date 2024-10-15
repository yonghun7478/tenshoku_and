package com.example.tokitoki.fake.repo

import com.example.tokitoki.data.local.UserInterestDao
import com.example.tokitoki.data.local.UserInterestWithCategoryEntity
import com.example.tokitoki.domain.converter.UserInterestConverter
import com.example.tokitoki.domain.model.UserInterest
import com.example.tokitoki.domain.repository.UserInterestRepository
import javax.inject.Inject

class FakeUserInterestRepositoryImpl @Inject constructor(
    private val userInterestDao: UserInterestDao
) : UserInterestRepository {
    override suspend fun getUserInterests(categoryId: Int): List<UserInterest> {
        var userInterestEntities = emptyList<UserInterestWithCategoryEntity>()
        when (categoryId) {
            1 -> {
                userInterestEntities = listOf(
                    // 趣味 (Hobby) 카테고리
                    UserInterestWithCategoryEntity(interestId = 1, interestTitle = "ヨガ1", interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg", categoryId = 1, categoryTitle = "趣味"),
                    UserInterestWithCategoryEntity(interestId = 2, interestTitle = "ヨガ2", interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg", categoryId = 1, categoryTitle = "趣味"),
                    UserInterestWithCategoryEntity(interestId = 3, interestTitle = "ヨガ3", interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg", categoryId = 1, categoryTitle = "趣味"),
                    UserInterestWithCategoryEntity(interestId = 4, interestTitle = "Hobby Crafting", interestUrl = "https://example.com/hobby4", categoryId = 1, categoryTitle = "趣味"),
                    UserInterestWithCategoryEntity(interestId = 5, interestTitle = "Hobby Gaming", interestUrl = "https://example.com/hobby5", categoryId = 1, categoryTitle = "趣味"),
                    UserInterestWithCategoryEntity(interestId = 6, interestTitle = "ヨガ1", interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg", categoryId = 1, categoryTitle = "趣味"),
                    UserInterestWithCategoryEntity(interestId = 7, interestTitle = "ヨガ2", interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg", categoryId = 1, categoryTitle = "趣味"),
                    UserInterestWithCategoryEntity(interestId = 8, interestTitle = "ヨガ3", interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg", categoryId = 1, categoryTitle = "趣味"),
                    UserInterestWithCategoryEntity(interestId = 9, interestTitle = "Hobby Crafting", interestUrl = "https://example.com/hobby4", categoryId = 1, categoryTitle = "趣味"),
                    UserInterestWithCategoryEntity(interestId = 10, interestTitle = "Hobby Gaming", interestUrl = "https://example.com/hobby5", categoryId = 1, categoryTitle = "趣味"),
                )
            }
            2 -> {
                userInterestEntities = listOf(
                    // 趣味 (Hobby) 카테고리
                    UserInterestWithCategoryEntity(interestId = 1, interestTitle = "ヨガ1", interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg", categoryId = 2, categoryTitle = "ライフスタイル"),
                    UserInterestWithCategoryEntity(interestId = 2, interestTitle = "ヨガ2", interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg", categoryId = 2, categoryTitle = "ライフスタイル"),
                    UserInterestWithCategoryEntity(interestId = 3, interestTitle = "ヨガ3", interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg", categoryId = 2, categoryTitle = "ライフスタイル"),
                )
            }
            else -> {
                userInterestEntities = listOf(
                    // 趣味 (Hobby) 카테고리
                    UserInterestWithCategoryEntity(interestId = 1, interestTitle = "ヨガ1", interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg", categoryId = 3, categoryTitle = "価値観"),
                    UserInterestWithCategoryEntity(interestId = 2, interestTitle = "ヨガ2", interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg", categoryId = 3, categoryTitle = "価値観"),
                    UserInterestWithCategoryEntity(interestId = 3, interestTitle = "ヨガ3", interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg", categoryId = 3, categoryTitle = "価値観"),
                )
            }
        }

        return userInterestEntities.map { UserInterestConverter.dataToDomain(it) }
    }
}