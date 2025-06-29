package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.AshiatoPage
import com.example.tokitoki.domain.repository.AshiatoRepository
import javax.inject.Inject

/**
 * 아시아토 목록 페이지를 가져오는 UseCase 인터페이스.
 * Presentation Layer(ViewModel 등)는 이 인터페이스에 의존합니다.
 */
interface GetAshiatoPageUseCase {
    /**
     * UseCase를 함수처럼 호출할 수 있게 해주는 invoke operator.
     * Repository의 getAshiatoPage 함수를 호출하여 결과를 반환합니다.
     *
     * @param cursor 다음 페이지 커서 (첫 페이지는 null)
     * @param limit 페이지당 항목 수 (선택 사항)
     * @return Result<AshiatoPage> 페이징된 아시아토 데이터 또는 오류
     */
    suspend operator fun invoke(cursor: String?, limit: Int? = null): Result<AshiatoPage>
}

/**
 * GetAshiatoPageUseCase 인터페이스의 구현체.
 * Hilt를 통해 repository가 주입됩니다.
 *
 * @property repository AshiatoRepository 인터페이스의 구현체 (DI를 통해 주입받음)
 */
class GetAshiatoPageUseCaseImpl @Inject constructor( // Hilt가 생성자를 통해 주입할 수 있도록 @Inject 추가
    private val repository: AshiatoRepository
) : GetAshiatoPageUseCase { // 인터페이스 구현

    override suspend operator fun invoke(cursor: String?, limit: Int?): Result<AshiatoPage> {
        return repository.getAshiatoPage(cursor, limit)
    }
}
