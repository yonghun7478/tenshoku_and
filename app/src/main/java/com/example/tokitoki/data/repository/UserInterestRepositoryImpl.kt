package com.example.tokitoki.data.repository

import com.example.tokitoki.data.local.UserInterestDao
import com.example.tokitoki.data.local.UserInterestWithCategoryEntity
import com.example.tokitoki.domain.converter.UserInterestConverter
import com.example.tokitoki.domain.model.UserInterest
import com.example.tokitoki.domain.repository.UserInterestRepository
import javax.inject.Inject

class UserInterestRepositoryImpl @Inject constructor(
    private val userInterestDao: UserInterestDao
) : UserInterestRepository {
    override suspend fun getUserInterests(categoryId: Int): List<UserInterest> {
//        val userInterestEntities = userInterestDao.getUserInterestsWithCategory(categoryId)
        var userInterestEntities = emptyList<UserInterestWithCategoryEntity>()
        if(categoryId == 1) {
            userInterestEntities = listOf(
                // 趣味 (Hobby) 카테고리
                UserInterestWithCategoryEntity(interestId = 1, interestTitle = "ヨガ1", interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg", categoryId = 1, categoryTitle = "趣味"),
                UserInterestWithCategoryEntity(interestId = 2, interestTitle = "ヨガ2", interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg", categoryId = 1, categoryTitle = "趣味"),
                UserInterestWithCategoryEntity(interestId = 3, interestTitle = "ヨガ3", interestUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg", categoryId = 1, categoryTitle = "趣味"),
                UserInterestWithCategoryEntity(interestId = 4, interestTitle = "Hobby Crafting", interestUrl = "https://example.com/hobby4", categoryId = 1, categoryTitle = "趣味"),
                UserInterestWithCategoryEntity(interestId = 5, interestTitle = "Hobby Gaming", interestUrl = "https://example.com/hobby5", categoryId = 1, categoryTitle = "趣味"),
                UserInterestWithCategoryEntity(interestId = 6, interestTitle = "Hobby Painting", interestUrl = "https://example.com/hobby6", categoryId = 1, categoryTitle = "趣味"),
                UserInterestWithCategoryEntity(interestId = 7, interestTitle = "Hobby Photography", interestUrl = "https://example.com/hobby7", categoryId = 1, categoryTitle = "趣味"),
                UserInterestWithCategoryEntity(interestId = 8, interestTitle = "Hobby Reading", interestUrl = "https://example.com/hobby8", categoryId = 1, categoryTitle = "趣味"),
                UserInterestWithCategoryEntity(interestId = 9, interestTitle = "Hobby Writing", interestUrl = "https://example.com/hobby9", categoryId = 1, categoryTitle = "趣味"),
                UserInterestWithCategoryEntity(interestId = 10, interestTitle = "Hobby Cooking", interestUrl = "https://example.com/hobby10", categoryId = 1, categoryTitle = "趣味"),
                UserInterestWithCategoryEntity(interestId = 11, interestTitle = "Hobby Traveling", interestUrl = "https://example.com/hobby11", categoryId = 1, categoryTitle = "趣味"),
                UserInterestWithCategoryEntity(interestId = 12, interestTitle = "Hobby Fishing", interestUrl = "https://example.com/hobby12", categoryId = 1, categoryTitle = "趣味"),
                UserInterestWithCategoryEntity(interestId = 13, interestTitle = "Hobby Dancing", interestUrl = "https://example.com/hobby13", categoryId = 1, categoryTitle = "趣味"),
                UserInterestWithCategoryEntity(interestId = 14, interestTitle = "Hobby Gardening", interestUrl = "https://example.com/hobby14", categoryId = 1, categoryTitle = "趣味"),
                UserInterestWithCategoryEntity(interestId = 15, interestTitle = "Hobby Collecting", interestUrl = "https://example.com/hobby15", categoryId = 1, categoryTitle = "趣味"),
                UserInterestWithCategoryEntity(interestId = 16, interestTitle = "Hobby DIY Projects", interestUrl = "https://example.com/hobby16", categoryId = 1, categoryTitle = "趣味"),
                UserInterestWithCategoryEntity(interestId = 17, interestTitle = "Hobby Sports", interestUrl = "https://example.com/hobby17", categoryId = 1, categoryTitle = "趣味"),
                UserInterestWithCategoryEntity(interestId = 18, interestTitle = "Hobby Hiking", interestUrl = "https://example.com/hobby18", categoryId = 1, categoryTitle = "趣味"),
                UserInterestWithCategoryEntity(interestId = 19, interestTitle = "Hobby Camping", interestUrl = "https://example.com/hobby19", categoryId = 1, categoryTitle = "趣味"),
                UserInterestWithCategoryEntity(interestId = 20, interestTitle = "Hobby Biking", interestUrl = "https://example.com/hobby20", categoryId = 1, categoryTitle = "趣味"),
            )
        } else if(categoryId == 2) {
            userInterestEntities = listOf(
                // 趣味 (Hobby) 카테고리
                UserInterestWithCategoryEntity(interestId = 21, interestTitle = "Healthy Lifestyle", interestUrl = "https://example.com/lifestyle1", categoryId = 2, categoryTitle = "ライフスタイル"),
                UserInterestWithCategoryEntity(interestId = 22, interestTitle = "Minimalist Lifestyle", interestUrl = "https://example.com/lifestyle2", categoryId = 2, categoryTitle = "ライフスタイル"),
                UserInterestWithCategoryEntity(interestId = 23, interestTitle = "Sustainable Living", interestUrl = "https://example.com/lifestyle3", categoryId = 2, categoryTitle = "ライフスタイル"),
                UserInterestWithCategoryEntity(interestId = 24, interestTitle = "Urban Lifestyle", interestUrl = "https://example.com/lifestyle4", categoryId = 2, categoryTitle = "ライフスタイル"),
                UserInterestWithCategoryEntity(interestId = 25, interestTitle = "Digital Nomad Lifestyle", interestUrl = "https://example.com/lifestyle5", categoryId = 2, categoryTitle = "ライフスタイル"),
                UserInterestWithCategoryEntity(interestId = 26, interestTitle = "Eco-friendly Lifestyle", interestUrl = "https://example.com/lifestyle6", categoryId = 2, categoryTitle = "ライフスタイル"),
                UserInterestWithCategoryEntity(interestId = 27, interestTitle = "Active Lifestyle", interestUrl = "https://example.com/lifestyle7", categoryId = 2, categoryTitle = "ライフスタイル"),
                UserInterestWithCategoryEntity(interestId = 28, interestTitle = "Luxury Lifestyle", interestUrl = "https://example.com/lifestyle8", categoryId = 2, categoryTitle = "ライフスタイル"),
                UserInterestWithCategoryEntity(interestId = 29, interestTitle = "Vegan Lifestyle", interestUrl = "https://example.com/lifestyle9", categoryId = 2, categoryTitle = "ライフスタイル"),
                UserInterestWithCategoryEntity(interestId = 30, interestTitle = "Nomadic Lifestyle", interestUrl = "https://example.com/lifestyle10", categoryId = 2, categoryTitle = "ライフスタイル"),
                UserInterestWithCategoryEntity(interestId = 31, interestTitle = "Quiet Lifestyle", interestUrl = "https://example.com/lifestyle11", categoryId = 2, categoryTitle = "ライフスタイル"),
                UserInterestWithCategoryEntity(interestId = 32, interestTitle = "Mindful Living", interestUrl = "https://example.com/lifestyle12", categoryId = 2, categoryTitle = "ライフスタイル"),
                UserInterestWithCategoryEntity(interestId = 33, interestTitle = "Outdoor Lifestyle", interestUrl = "https://example.com/lifestyle13", categoryId = 2, categoryTitle = "ライフスタイル"),
                UserInterestWithCategoryEntity(interestId = 34, interestTitle = "Frugal Lifestyle", interestUrl = "https://example.com/lifestyle14", categoryId = 2, categoryTitle = "ライフスタイル"),
                UserInterestWithCategoryEntity(interestId = 35, interestTitle = "Fitness Lifestyle", interestUrl = "https://example.com/lifestyle15", categoryId = 2, categoryTitle = "ライフスタイル"),
                UserInterestWithCategoryEntity(interestId = 36, interestTitle = "Holistic Lifestyle", interestUrl = "https://example.com/lifestyle16", categoryId = 2, categoryTitle = "ライフスタイル"),
                UserInterestWithCategoryEntity(interestId = 37, interestTitle = "Balanced Lifestyle", interestUrl = "https://example.com/lifestyle17", categoryId = 2, categoryTitle = "ライフスタイル"),
                UserInterestWithCategoryEntity(interestId = 38, interestTitle = "Creative Lifestyle", interestUrl = "https://example.com/lifestyle18", categoryId = 2, categoryTitle = "ライフスタイル"),
                UserInterestWithCategoryEntity(interestId = 39, interestTitle = "Social Lifestyle", interestUrl = "https://example.com/lifestyle19", categoryId = 2, categoryTitle = "ライフスタイル"),
                UserInterestWithCategoryEntity(interestId = 40, interestTitle = "Cultural Lifestyle", interestUrl = "https://example.com/lifestyle20", categoryId = 2, categoryTitle = "ライフスタイル"),
            )
        } else {
            userInterestEntities = listOf(
                // 趣味 (Hobby) 카테고리
                UserInterestWithCategoryEntity(interestId = 41, interestTitle = "Honesty and Integrity", interestUrl = "https://example.com/values1", categoryId = 3, categoryTitle = "価値観"),
                UserInterestWithCategoryEntity(interestId = 42, interestTitle = "Compassion and Empathy", interestUrl = "https://example.com/values2", categoryId = 3, categoryTitle = "価値観"),
                UserInterestWithCategoryEntity(interestId = 43, interestTitle = "Respect for Others", interestUrl = "https://example.com/values3", categoryId = 3, categoryTitle = "価値観"),
                UserInterestWithCategoryEntity(interestId = 44, interestTitle = "Commitment and Dedication", interestUrl = "https://example.com/values4", categoryId = 3, categoryTitle = "価値観"),
                UserInterestWithCategoryEntity(interestId = 45, interestTitle = "Responsibility", interestUrl = "https://example.com/values5", categoryId = 3, categoryTitle = "価値観"),
                UserInterestWithCategoryEntity(interestId = 46, interestTitle = "Accountability", interestUrl = "https://example.com/values6", categoryId = 3, categoryTitle = "価値観"),
                UserInterestWithCategoryEntity(interestId = 47, interestTitle = "Gratitude and Appreciation", interestUrl = "https://example.com/values7", categoryId = 3, categoryTitle = "価値観"),
                UserInterestWithCategoryEntity(interestId = 48, interestTitle = "Perseverance", interestUrl = "https://example.com/values8", categoryId = 3, categoryTitle = "価値観"),
                UserInterestWithCategoryEntity(interestId = 49, interestTitle = "Innovation and Creativity", interestUrl = "https://example.com/values9", categoryId = 3, categoryTitle = "価値観"),
                UserInterestWithCategoryEntity(interestId = 50, interestTitle = "Respect for Nature", interestUrl = "https://example.com/values10", categoryId = 3, categoryTitle = "価値観")
            )
        }

        return userInterestEntities.map { UserInterestConverter.dataToDomain(it) }
    }
}