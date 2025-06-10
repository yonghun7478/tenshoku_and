package com.example.tokitoki.domain.usecase.tag

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.TagRepository
import javax.inject.Inject

/**
 * 특정 ID를 가진 태그 목록을 가져오는 UseCase 인터페이스
 */
interface GetTagsUseCase {
    /**
     * UseCase를 실행합니다.
     * @param tagIds 조회할 태그 ID 목록
     * @return MainHomeTag 목록
     */
    suspend operator fun invoke(tagIds: List<Int>): List<MainHomeTag>
}

/**
 * GetTagsUseCase 인터페이스의 구현체
 */
class GetTagsUseCaseImpl @Inject constructor(
    private val tagRepository: TagRepository
) : GetTagsUseCase {
    override suspend operator fun invoke(tagIds: List<Int>): List<MainHomeTag> {
        return tagRepository.getTags(tagIds)
    }
} 