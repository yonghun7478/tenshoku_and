package com.example.tokitoki.domain.converter

import com.example.tokitoki.data.local.MyTagEntity
import com.example.tokitoki.domain.model.MyTag

object MyTagConverter {

    // MyTagEntity -> MyTag
    fun entityToDomain(entity: MyTagEntity): MyTag {
        return MyTag(
            tagId = entity.tagId,
            tagTypeId = entity.tagTypeId
        )
    }

    // MyTag -> MyTagEntity
    fun domainToEntity(domain: MyTag): MyTagEntity {
        return MyTagEntity(
            tagId = domain.tagId,
            tagTypeId = domain.tagTypeId
        )
    }
}
