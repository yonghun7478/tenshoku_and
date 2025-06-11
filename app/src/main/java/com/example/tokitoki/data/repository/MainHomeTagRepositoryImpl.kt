package com.example.tokitoki.data.repository

import com.example.tokitoki.domain.model.TagType
import com.example.tokitoki.data.model.toDomain
import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import com.example.tokitoki.domain.repository.TagRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MainHomeTagRepositoryImpl @Inject constructor(
    private val tagRepository: TagRepository
) : MainHomeTagRepository {

    override suspend fun getTodayTag(): Result<MainHomeTag> {
        delay(500)
        val allTags = tagRepository.getAllTags().getOrThrow()
        return Result.success(allTags[0])
    }

    override suspend fun getTrendingTags(): Result<List<MainHomeTag>> {
        delay(700)
        val allTags = tagRepository.getAllTags().getOrThrow()
        return Result.success(listOf(
            allTags[1],
            allTags[2],
            allTags[3],
            allTags[4],
            allTags[5],
            allTags[6]
        ))
    }

    override suspend fun getMyTags(): Result<List<MainHomeTag>> {
        delay(300)
        val allTags = tagRepository.getAllTags().getOrThrow()
        return Result.success(listOf(
            allTags[7],
            allTags[8],
            allTags[9],
            allTags[10],
            allTags[11]
        ))
    }

    override suspend fun getSuggestedTags(): Result<List<MainHomeTag>> {
        delay(400)
        val allTags = tagRepository.getAllTags().getOrThrow()
        return Result.success(listOf(
            allTags[12],
            allTags[13],
            allTags[14],
            allTags[15],
            allTags[16],
            allTags[17]
        ))
    }

    override suspend fun getTagsByCategory(categoryId: String): Result<List<MainHomeTag>> {
        return try {
            delay(1000)
            val tags = tagRepository.getAllTags().getOrThrow().filter { it.categoryId == categoryId }
            Result.success(tags)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTagsByQuery(query: String): Result<List<MainHomeTag>> {
        return try {
            delay(500)
            val filtered = tagRepository.getAllTags().getOrThrow().filter {
                it.name.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true)
            }
            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyTagsByType(userId: String, tagType: TagType): Result<List<MainHomeTag>> {
        return try {
            delay(500)
            // 현재는 임시로 myTags에서 타입별로 필터링
            // TODO: 실제 구현에서는 userId를 사용하여 해당 사용자의 마이태그만 필터링해야 함
            val allTags = tagRepository.getAllTags().getOrThrow()
            val filtered = allTags.filter { it.tagType == tagType }.take(5) // 임시로 5개만 반환
            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTagDetail(tagId: String): Result<MainHomeTag> {
        return try {
            delay(500)
            val tag = tagRepository.getAllTags().getOrThrow().find { it.id == tagId }
            if (tag != null) {
                Result.success(tag)
            } else {
                Result.failure(Exception("Tag not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserTags(userId: String): Result<List<MainHomeTag>> {
        // 더미 데이터: 사용자의 모든 태그를 가져오는 로직 (myTags 리스트 사용)
        // TODO: 실제 API 구현 시 해당 사용자의 태그 목록을 가져오도록 수정 필요
        delay(200) // API 호출 시뮬레이션을 위한 약간의 딜레이
        val allTags = tagRepository.getAllTags().getOrThrow()
        val myTags = listOf(
            allTags[7],   // 写真撮影
            allTags[8],   // アート好き
            allTags[9],   // ガーデニング
            allTags[10],
            allTags[11]
        )
        return Result.success(myTags)
    }
}