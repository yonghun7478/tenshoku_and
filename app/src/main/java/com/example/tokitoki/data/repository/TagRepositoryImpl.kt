package com.example.tokitoki.data.repository

import com.example.tokitoki.data.local.CategoryDao
import com.example.tokitoki.data.local.CategoryEntity
import com.example.tokitoki.data.local.TagDao
import com.example.tokitoki.data.local.TagWithCategoryEntity
import com.example.tokitoki.domain.converter.CategoryConverter
import com.example.tokitoki.domain.converter.TagConverter
import com.example.tokitoki.domain.model.Category
import com.example.tokitoki.domain.model.Tag
import com.example.tokitoki.domain.repository.TagRepository
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val tagDao: TagDao
) : TagRepository {

    override suspend fun getCategories(): List<Category> {
        val categoryEntities = listOf(
            CategoryEntity(id = 1, title = "趣味"),          // "Hobby"
            CategoryEntity(id = 2, title = "ライフスタイル"),  // "Lifestyle"
            CategoryEntity(id = 3, title = "価値観")          // "Values"
        )
        return categoryEntities.map { CategoryConverter.dataToDomain(it) }
    }

    override suspend fun getTags(categoryId: Int): List<Tag> {
        var tagEntities = emptyList<TagWithCategoryEntity>()
        if(categoryId == 1) {
            tagEntities = listOf(
                // 趣味 (Hobby) 카테고리
                TagWithCategoryEntity(tagId = 1, tagTitle = "ヨガ1", tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg", categoryId = 1, categoryTitle = "趣味"),
                TagWithCategoryEntity(tagId = 2, tagTitle = "ヨガ2", tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg", categoryId = 1, categoryTitle = "趣味"),
                TagWithCategoryEntity(tagId = 3, tagTitle = "ヨガ3", tagUrl = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg", categoryId = 1, categoryTitle = "趣味"),
                TagWithCategoryEntity(tagId = 4, tagTitle = "Hobby Crafting", tagUrl = "https://example.com/hobby4", categoryId = 1, categoryTitle = "趣味"),
                TagWithCategoryEntity(tagId = 5, tagTitle = "Hobby Gaming", tagUrl = "https://example.com/hobby5", categoryId = 1, categoryTitle = "趣味"),
                TagWithCategoryEntity(tagId = 6, tagTitle = "Hobby Painting", tagUrl = "https://example.com/hobby6", categoryId = 1, categoryTitle = "趣味"),
                TagWithCategoryEntity(tagId = 7, tagTitle = "Hobby Photography", tagUrl = "https://example.com/hobby7", categoryId = 1, categoryTitle = "趣味"),
                TagWithCategoryEntity(tagId = 8, tagTitle = "Hobby Reading", tagUrl = "https://example.com/hobby8", categoryId = 1, categoryTitle = "趣味"),
                TagWithCategoryEntity(tagId = 9, tagTitle = "Hobby Writing", tagUrl = "https://example.com/hobby9", categoryId = 1, categoryTitle = "趣味"),
                TagWithCategoryEntity(tagId = 10, tagTitle = "Hobby Cooking", tagUrl = "https://example.com/hobby10", categoryId = 1, categoryTitle = "趣味"),
                TagWithCategoryEntity(tagId = 11, tagTitle = "Hobby Traveling", tagUrl = "https://example.com/hobby11", categoryId = 1, categoryTitle = "趣味"),
                TagWithCategoryEntity(tagId = 12, tagTitle = "Hobby Fishing", tagUrl = "https://example.com/hobby12", categoryId = 1, categoryTitle = "趣味"),
                TagWithCategoryEntity(tagId = 13, tagTitle = "Hobby Dancing", tagUrl = "https://example.com/hobby13", categoryId = 1, categoryTitle = "趣味"),
                TagWithCategoryEntity(tagId = 14, tagTitle = "Hobby Gardening", tagUrl = "https://example.com/hobby14", categoryId = 1, categoryTitle = "趣味"),
                TagWithCategoryEntity(tagId = 15, tagTitle = "Hobby Collecting", tagUrl = "https://example.com/hobby15", categoryId = 1, categoryTitle = "趣味"),
                TagWithCategoryEntity(tagId = 16, tagTitle = "Hobby DIY Projects", tagUrl = "https://example.com/hobby16", categoryId = 1, categoryTitle = "趣味"),
                TagWithCategoryEntity(tagId = 17, tagTitle = "Hobby Sports", tagUrl = "https://example.com/hobby17", categoryId = 1, categoryTitle = "趣味"),
                TagWithCategoryEntity(tagId = 18, tagTitle = "Hobby Hiking", tagUrl = "https://example.com/hobby18", categoryId = 1, categoryTitle = "趣味"),
                TagWithCategoryEntity(tagId = 19, tagTitle = "Hobby Camping", tagUrl = "https://example.com/hobby19", categoryId = 1, categoryTitle = "趣味"),
                TagWithCategoryEntity(tagId = 20, tagTitle = "Hobby Biking", tagUrl = "https://example.com/hobby20", categoryId = 1, categoryTitle = "趣味"),
            )
        } else if(categoryId == 2) {
            tagEntities = listOf(
                // 趣味 (Hobby) 카테고리
                TagWithCategoryEntity(tagId = 21, tagTitle = "Healthy Lifestyle", tagUrl = "https://example.com/lifestyle1", categoryId = 2, categoryTitle = "ライフスタイル"),
                TagWithCategoryEntity(tagId = 22, tagTitle = "Minimalist Lifestyle", tagUrl = "https://example.com/lifestyle2", categoryId = 2, categoryTitle = "ライフスタイル"),
                TagWithCategoryEntity(tagId = 23, tagTitle = "Sustainable Living", tagUrl = "https://example.com/lifestyle3", categoryId = 2, categoryTitle = "ライフスタイル"),
                TagWithCategoryEntity(tagId = 24, tagTitle = "Urban Lifestyle", tagUrl = "https://example.com/lifestyle4", categoryId = 2, categoryTitle = "ライフスタイル"),
                TagWithCategoryEntity(tagId = 25, tagTitle = "Digital Nomad Lifestyle", tagUrl = "https://example.com/lifestyle5", categoryId = 2, categoryTitle = "ライフスタイル"),
                TagWithCategoryEntity(tagId = 26, tagTitle = "Eco-friendly Lifestyle", tagUrl = "https://example.com/lifestyle6", categoryId = 2, categoryTitle = "ライフスタイル"),
                TagWithCategoryEntity(tagId = 27, tagTitle = "Active Lifestyle", tagUrl = "https://example.com/lifestyle7", categoryId = 2, categoryTitle = "ライフスタイル"),
                TagWithCategoryEntity(tagId = 28, tagTitle = "Luxury Lifestyle", tagUrl = "https://example.com/lifestyle8", categoryId = 2, categoryTitle = "ライフスタイル"),
                TagWithCategoryEntity(tagId = 29, tagTitle = "Vegan Lifestyle", tagUrl = "https://example.com/lifestyle9", categoryId = 2, categoryTitle = "ライフスタイル"),
                TagWithCategoryEntity(tagId = 30, tagTitle = "Nomadic Lifestyle", tagUrl = "https://example.com/lifestyle10", categoryId = 2, categoryTitle = "ライフスタイル"),
                TagWithCategoryEntity(tagId = 31, tagTitle = "Quiet Lifestyle", tagUrl = "https://example.com/lifestyle11", categoryId = 2, categoryTitle = "ライフスタイル"),
                TagWithCategoryEntity(tagId = 32, tagTitle = "Mindful Living", tagUrl = "https://example.com/lifestyle12", categoryId = 2, categoryTitle = "ライフスタイル"),
                TagWithCategoryEntity(tagId = 33, tagTitle = "Outdoor Lifestyle", tagUrl = "https://example.com/lifestyle13", categoryId = 2, categoryTitle = "ライフスタイル"),
                TagWithCategoryEntity(tagId = 34, tagTitle = "Frugal Lifestyle", tagUrl = "https://example.com/lifestyle14", categoryId = 2, categoryTitle = "ライフスタイル"),
                TagWithCategoryEntity(tagId = 35, tagTitle = "Fitness Lifestyle", tagUrl = "https://example.com/lifestyle15", categoryId = 2, categoryTitle = "ライフスタイル"),
                TagWithCategoryEntity(tagId = 36, tagTitle = "Holistic Lifestyle", tagUrl = "https://example.com/lifestyle16", categoryId = 2, categoryTitle = "ライフスタイル"),
                TagWithCategoryEntity(tagId = 37, tagTitle = "Balanced Lifestyle", tagUrl = "https://example.com/lifestyle17", categoryId = 2, categoryTitle = "ライフスタイル"),
                TagWithCategoryEntity(tagId = 38, tagTitle = "Creative Lifestyle", tagUrl = "https://example.com/lifestyle18", categoryId = 2, categoryTitle = "ライフスタイル"),
                TagWithCategoryEntity(tagId = 39, tagTitle = "Social Lifestyle", tagUrl = "https://example.com/lifestyle19", categoryId = 2, categoryTitle = "ライフスタイル"),
                TagWithCategoryEntity(tagId = 40, tagTitle = "Cultural Lifestyle", tagUrl = "https://example.com/lifestyle20", categoryId = 2, categoryTitle = "ライフスタイル"),
            )
        } else {
            tagEntities = listOf(
                // 趣味 (Hobby) 카테고리
                TagWithCategoryEntity(tagId = 41, tagTitle = "Honesty and Integrity", tagUrl = "https://example.com/values1", categoryId = 3, categoryTitle = "価値観"),
                TagWithCategoryEntity(tagId = 42, tagTitle = "Compassion and Empathy", tagUrl = "https://example.com/values2", categoryId = 3, categoryTitle = "価値観"),
                TagWithCategoryEntity(tagId = 43, tagTitle = "Respect for Others", tagUrl = "https://example.com/values3", categoryId = 3, categoryTitle = "価値観"),
                TagWithCategoryEntity(tagId = 44, tagTitle = "Commitment and Dedication", tagUrl = "https://example.com/values4", categoryId = 3, categoryTitle = "価値観"),
                TagWithCategoryEntity(tagId = 45, tagTitle = "Responsibility", tagUrl = "https://example.com/values5", categoryId = 3, categoryTitle = "価値観"),
                TagWithCategoryEntity(tagId = 46, tagTitle = "Accountability", tagUrl = "https://example.com/values6", categoryId = 3, categoryTitle = "価値観"),
                TagWithCategoryEntity(tagId = 47, tagTitle = "Gratitude and Appreciation", tagUrl = "https://example.com/values7", categoryId = 3, categoryTitle = "価値観"),
                TagWithCategoryEntity(tagId = 48, tagTitle = "Perseverance", tagUrl = "https://example.com/values8", categoryId = 3, categoryTitle = "価値観"),
                TagWithCategoryEntity(tagId = 49, tagTitle = "Innovation and Creativity", tagUrl = "https://example.com/values9", categoryId = 3, categoryTitle = "価値観"),
                TagWithCategoryEntity(tagId = 50, tagTitle = "Respect for Nature", tagUrl = "https://example.com/values10", categoryId = 3, categoryTitle = "価値観")
            )
        }

        return tagEntities.map { TagConverter.dataToDomain(it) }
    }
}