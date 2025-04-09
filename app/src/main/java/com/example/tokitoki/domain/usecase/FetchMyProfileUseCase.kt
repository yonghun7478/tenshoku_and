package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.repository.MyProfileRepository
import javax.inject.Inject

// --- Fetch UseCase (신규) ---
/** API(또는 더미 데이터)에서 프로필을 가져오는 UseCase 인터페이스 */
interface FetchMyProfileUseCase {
    /** 성공 시 MyProfile, 실패 시 null 반환 */
    suspend operator fun invoke(): MyProfile?
}

/** FetchMyProfileUseCase 구현체 */
class FetchMyProfileUseCaseImpl @Inject constructor(
    private val myProfileRepository: MyProfileRepository
) : FetchMyProfileUseCase {
    override suspend operator fun invoke(): MyProfile? {
        return try {
            // Repository를 통해 데이터(API 또는 더미) 호출
            myProfileRepository.fetchUserProfileFromApi()
        } catch (e: Exception) {
            // 데이터 호출 중 에러 처리
            println("Error in FetchMyProfileUseCase: ${e.message}")
            null // 실패 시 null 반환
        }
    }
}