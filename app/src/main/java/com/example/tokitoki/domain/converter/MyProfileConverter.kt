package com.example.tokitoki.domain.converter

import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.model.MyTag
import com.example.tokitoki.ui.model.MyProfileItem
import com.example.tokitoki.ui.model.MyTagItem

object MyProfileConverter {

    // MyProfile -> MyProfileItem 변환
    fun domainToItem(domain: MyProfile): MyProfileItem {
        return MyProfileItem(
            name = domain.name,
            age = domain.age,
            thumbnailImageUri = domain.thumbnailImageUri,
            myTagItems = domain.myTagItems.map { tag ->
                tagToItem(tag)
            },
            mySelfSentence = domain.mySelfSentence
        )
    }

    // MyProfileItem -> MyProfile 변환
    fun itemToDomain(item: MyProfileItem): MyProfile {
        return MyProfile(
            name = item.name,
            age = item.age,
            thumbnailImageUri = item.thumbnailImageUri,
            myTagItems = item.myTagItems.map { tagItem ->
                itemToTag(tagItem)
            },
            mySelfSentence = item.mySelfSentence
        )
    }

    // MyTag -> MyTagItem 변환
    private fun tagToItem(tag: MyTag): MyTagItem {
        return MyTagItem(
            title = tag.title,
            url = tag.url,
            categoryTitle = tag.categoryTitle
        )
    }

    // MyTagItem -> MyTag 변환
    private fun itemToTag(tagItem: MyTagItem): MyTag {
        return MyTag(
            title = tagItem.title,
            url = tagItem.url,
            categoryTitle = tagItem.categoryTitle
        )
    }
}
