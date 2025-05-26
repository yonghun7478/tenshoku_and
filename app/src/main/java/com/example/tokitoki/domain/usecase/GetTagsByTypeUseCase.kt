package com.example.tokitoki.domain.usecase

import com.example.tokitoki.data.model.TagType
import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import javax.inject.Inject

/**
 * 사용자가 등록한 마이태그를 타입별로 조회하는 UseCase 인터페이스
 */
interface GetMyTagsByTypeUseCase {
    /**
     * UseCase를 실행합니다.
     * @param userId 사용자 ID
     * @param tagType 태그 타입 (HOBBY, LIFESTYLE, VALUE)
     * @return 사용자가 등록한 해당 타입의 마이태그 목록을 포함한 Result
     */
    suspend operator fun invoke(userId: String, tagType: TagType): Result<List<MainHomeTag>>
}

/**
 * GetMyTagsByTypeUseCase 인터페이스의 구현체
 */
class GetMyTagsByTypeUseCaseImpl @Inject constructor(
    private val repository: MainHomeTagRepository
) : GetMyTagsByTypeUseCase {
    override suspend operator fun invoke(userId: String, tagType: TagType): Result<List<MainHomeTag>> {
        return repository.getMyTagsByType(userId, tagType)
    }
} 