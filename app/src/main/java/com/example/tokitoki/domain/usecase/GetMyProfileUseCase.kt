package com.example.tokitoki.domain.usecase

import android.net.Uri
import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.model.MyTag

import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
) {
    suspend operator fun invoke(): MyProfile {
        val myProfile = MyProfile(
            name = "yonghun",
            age = "33",
            thumbnailImageUri = Uri.EMPTY,
            myTagItems = listOf(
                MyTag(
                    title = "テストタグ1",
                    url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                    categoryTitle = "テストカテゴリー1"
                ),
                MyTag(
                    title = "テストタグ2",
                    url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                    categoryTitle = "テストカテゴリー2"
                ),
                MyTag(
                    title = "テストタグ3",
                    url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                    categoryTitle = "テストカテゴリー3"
                ),
                MyTag(
                    title = "テストタグ3",
                    url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                    categoryTitle = "テストカテゴリー3"
                ),
                MyTag(
                    title = "テストタグ3",
                    url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                    categoryTitle = "テストカテゴリー3"
                ),
                MyTag(
                    title = "テストタグ3",
                    url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                    categoryTitle = "テストカテゴリー3"
                ),
                MyTag(
                    title = "テストタグ3",
                    url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                    categoryTitle = "テストカテゴリー3"
                ),
                MyTag(
                    title = "テストタグ3",
                    url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                    categoryTitle = "テストカテゴリー3"
                ),
            ),
            mySelfSentence = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus feugiat sapien quis turpis luctus, id convallis mauris malesuada. Ut tincidunt sapien risus, sit amet accumsan elit varius ut. Sed condimentum malesuada ultricies. In hac habitasse platea dictumst."
        )

        return myProfile
    }
}
