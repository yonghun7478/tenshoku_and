package com.example.tokitoki.domain.usecase.tag

import com.example.tokitoki.data.model.TagType
import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.TagRepository
import javax.inject.Inject

/**
 * 태그를 타입별로 조회하는 UseCase 인터페이스
 */
interface GetTagsByTypeUseCase {
    /**
     * UseCase를 실행합니다.
     * @param tagType 태그 타입 (HOBBY, LIFESTYLE, VALUE)
     * @return 해당 타입의 MainHomeTag 목록을 포함한 Result
     */
    suspend operator fun invoke(tagType: TagType): Result<List<MainHomeTag>>
}

/**
 * GetTagsByTypeUseCase 인터페이스의 구현체
 */
class GetTagsByTypeUseCaseImpl @Inject constructor(
    private val tagRepository: TagRepository
) : GetTagsByTypeUseCase {
    override suspend operator fun invoke(tagType: TagType): Result<List<MainHomeTag>> {
        return tagRepository.getTagsByType(tagType)
    }
} 