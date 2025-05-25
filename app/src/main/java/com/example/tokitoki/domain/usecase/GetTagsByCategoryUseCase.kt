package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import javax.inject.Inject

/**
 * 카테고리별 태그 목록을 가져오는 UseCase 인터페이스
 */
interface GetTagsByCategoryUseCase {
    /**
     * UseCase를 실행합니다.
     * @param categoryId 카테고리 ID
     * @return 카테고리에 속한 태그 목록을 포함한 Result
     */
    suspend operator fun invoke(categoryId: String): Result<List<MainHomeTag>>
}

/**
 * GetTagsByCategoryUseCase 인터페이스의 구현체
 */
class GetTagsByCategoryUseCaseImpl @Inject constructor(
    private val repository: MainHomeTagRepository
) : GetTagsByCategoryUseCase {
    override suspend operator fun invoke(categoryId: String): Result<List<MainHomeTag>> {
        return repository.getTagsByCategory(categoryId)
    }
} 