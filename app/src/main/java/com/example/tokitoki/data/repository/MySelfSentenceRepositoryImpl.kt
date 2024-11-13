package com.example.tokitoki.data.repository

import com.example.tokitoki.data.local.MySelfSentenceDao
import com.example.tokitoki.data.local.MySelfSentenceEntity
import com.example.tokitoki.domain.converter.MySelfSentenceConverter
import com.example.tokitoki.domain.model.MySelfSentence
import com.example.tokitoki.domain.repository.MySelfSentenceRepository
import javax.inject.Inject

class MySelfSentenceRepositoryImpl @Inject constructor(
    private val mySelfSentenceDao: MySelfSentenceDao,
) : MySelfSentenceRepository {
    override suspend fun getAllSentences(): List<MySelfSentence> {
        val myselfSentenceEntities = listOf(
            MySelfSentenceEntity(
                id = 1,
                type = "タイプ１",
                typeColor = "FF36C2CE",
                sentence = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n"
            ),
            MySelfSentenceEntity(
                id = 2,
                type = "タイプ2",
                typeColor = "FF4B89DC",
                sentence = "Curabitur pretium tincidunt lacus. Nulla gravida orci a odio. Nullam varius, turpis et commodo pharetra, est eros bibendum elit, nec luctus magna felis sollicitudin mauris. Integer in mauris eu nibh euismod gravida. Duis ac tellus et risus vulputate vehicula. Donec lobortis risus a elit. Etiam tempor. Ut ullamcorper, ligula eu tempor congue, eros est euismod turpis, id tincidunt sapien risus a quam. Maecenas fermentum consequat mi. Donec fermentum. Pellentesque malesuada nulla a mi. Duis sapien sem, aliquet nec, commodo eget, consequat quis, neque. Aliquam faucibus, elit ut dictum aliquet, felis nisl adipiscing sapien, sed malesuada diam lacus eget erat. Cras mollis scelerisque nunc. Nullam arcu. Aliquam consequat. \n"
            ),
            MySelfSentenceEntity(
                id = 3,
                type = "タイプ3",
                typeColor = "FFF35A1B",
                sentence = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus feugiat sapien quis turpis luctus, id convallis mauris malesuada. Ut tincidunt sapien risus, sit amet accumsan elit varius ut. Sed condimentum malesuada ultricies. In hac habitasse platea dictumst.\n"
            ),
        )

        return myselfSentenceEntities.map {
            MySelfSentenceConverter.entityToDomain(it)
        }
    }
}