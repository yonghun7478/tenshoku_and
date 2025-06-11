package com.example.tokitoki.ui.converter

import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.ui.model.MyProfileItem
import com.example.tokitoki.ui.model.MyTagItem

object MyProfileUiConverter {
    fun domainToUi(profile: MyProfile, age: String, tags: List<MainHomeTag>, mySelfSentence: String): MyProfileItem {
        return MyProfileItem(
            name = profile.name,
            age = age,
            mySelfSentence = mySelfSentence,
            myTagItems = tags.map { tag ->
                MyTagItem(
                    tagId = tag.id.toIntOrNull() ?: 0,
                    categoryId = tag.categoryId.toIntOrNull() ?: 0,
                    title = tag.name,
                    url = tag.imageUrl,
                    categoryTitle = tag.tagType.value
                )
            }
        )
    }
}
