package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MainHomeTagCategory
import com.example.tokitoki.domain.repository.MainHomeTagCategoryRepository
import javax.inject.Inject

/**
 * 태그 카테고리 목록을 가져오는 UseCase 인터페이스
 */
interface GetTagCategoriesUseCase {
    /**
     * UseCase를 실행합니다.
     * @return 태그 카테고리 목록을 포함한 Result
     */
    suspend operator fun invoke(): Result<List<MainHomeTagCategory>>
}

/**
 * GetTagCategoriesUseCase 인터페이스의 구현체
 */
class GetTagCategoriesUseCaseImpl @Inject constructor(
    private val repository: MainHomeTagCategoryRepository
) : GetTagCategoriesUseCase {
    override suspend operator fun invoke(): Result<List<MainHomeTagCategory>> {
        return repository.getCategories()
    }
} 