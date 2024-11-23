package com.example.tokitoki.ui.converter

import android.net.Uri
import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.model.MyTag
import com.example.tokitoki.ui.model.MyProfileItem
import com.example.tokitoki.ui.model.MyTagItem

object MyProfileUiConverter {

    // MyProfile + List<MyTag> -> MyProfileItem
    fun domainToUi(profile: MyProfile, tags: List<MyTag>): MyProfileItem {
        return MyProfileItem(
            name = profile.name,
            age = profile.age.toString(),
            thumbnailImageUri = Uri.EMPTY,
            mySelfSentence = profile.mySelfSentence,
            myTagItems = tags.map { tag ->
                MyTagItem(
                    title = "Tag ${tag.tagId}", // 임의의 태그 제목
                    url = "https://example.com/tag/${tag.tagId}", // 태그 URL 생성
                    categoryTitle = "Category for ${tag.tagId}" // 임의의 카테고리 제목
                )
            }
        )
    }
}
