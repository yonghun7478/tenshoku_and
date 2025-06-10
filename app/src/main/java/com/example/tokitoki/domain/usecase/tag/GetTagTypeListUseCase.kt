package com.example.tokitoki.domain.usecase.tag

import com.example.tokitoki.data.model.TagType
import com.example.tokitoki.domain.repository.TagRepository
import javax.inject.Inject

/**
 * 태그 타입 목록을 가져오는 UseCase 인터페이스
 */
interface GetTagTypeListUseCase {
    /**
     * UseCase를 실행합니다.
     * @return TagType 목록
     */
    suspend operator fun invoke(): List<TagType>
}

/**
 * GetTagTypeListUseCase 인터페이스의 구현체
 */
class GetTagTypeListUseCaseImpl @Inject constructor(
    private val tagRepository: TagRepository
) : GetTagTypeListUseCase {
    override suspend operator fun invoke(): List<TagType> {
        return tagRepository.getTagTypeList()
    }
} 