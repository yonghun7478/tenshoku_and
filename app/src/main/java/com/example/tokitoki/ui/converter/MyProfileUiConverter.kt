package com.example.tokitoki.ui.converter

import android.net.Uri
import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.model.Tag
import com.example.tokitoki.ui.model.MyProfileItem
import com.example.tokitoki.ui.model.MyTagItem

object MyProfileUiConverter {
    fun domainToUi(profile: MyProfile, tags: List<Tag>): MyProfileItem {
        return MyProfileItem(
            name = profile.name,
            age = profile.age.toString(),
            thumbnailImageUri = Uri.EMPTY,
            mySelfSentence = profile.mySelfSentence,
            myTagItems = tags.map { tag ->
                MyTagItem(
                    title = tag.title, // 임의의 태그 제목
                    url = tag.url, // 태그 URL 생성
                    categoryTitle = tag.categoryTitle // 임의의 카테고리 제목
                )
            }
        )
    }
}
