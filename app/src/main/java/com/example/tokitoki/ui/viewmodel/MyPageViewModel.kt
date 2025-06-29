package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.GetMyProfileUseCase
import com.example.tokitoki.domain.usecase.GetMySelfSentenceUseCase
import com.example.tokitoki.domain.usecase.ClearTokensUseCase
import com.example.tokitoki.domain.usecase.DeleteUserProfileUseCase
import com.example.tokitoki.domain.usecase.ClearMyTagUseCase
import com.example.tokitoki.ui.state.MyPageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import java.time.LocalDate
import java.time.Period

// ViewModel
@HiltViewModel
class MyPageViewModel @Inject constructor(
    // UseCase 의존성 변경: Fetch와 Save 분리
    private val getMyProfileUseCase: GetMyProfileUseCase,     // 기존 Local Get UseCase
    private val getMySelfSentenceUseCase: GetMySelfSentenceUseCase, // 기존 Sentence Get UseCase
    private val clearTokensUseCase: ClearTokensUseCase, // 추가
    private val deleteUserProfileUseCase: DeleteUserProfileUseCase, // 추가
    private val clearMyTagUseCase: ClearMyTagUseCase, // 추가
) : ViewModel() {

    private val _myPageState = MutableStateFlow(MyPageState())
    val myPageState: StateFlow<MyPageState> = _myPageState.asStateFlow()

    private val _logoutCompleted = MutableSharedFlow<Unit>()
    val logoutCompleted: SharedFlow<Unit> = _logoutCompleted

    fun loadMyPageData() {
        viewModelScope.launch {
            _myPageState.update { it.copy(isLoading = true, error = null) }

            try {
                var localProfile = getMyProfileUseCase() // 로컬 프로필 가져오기

                val calculatedAge = calculateAge(localProfile.birthDay) // 생년월일로 나이 계산
                var bioSentence: String? = null
                // localProfile은 이제 DB값이거나 위에서 생성된 더미값일 수 있음
                if (localProfile.mySelfSentenceId > 0) {
                    try {
                        val selfSentence = getMySelfSentenceUseCase(localProfile.mySelfSentenceId)
                        bioSentence = selfSentence.sentence
                    } catch (e: Exception) {
                        // --- 수정된 부분 시작 ---
                        println("Error fetching self sentence (id: ${localProfile.mySelfSentenceId}): ${e.message}")
                        // 에러 발생 시 더미 bio 할당
                        bioSentence = "자기소개 로딩 실패 (더미 데이터)"
                        // --- 수정된 부분 끝 ---
                    }
                }

                // 4. 최종 UI State 업데이트
                _myPageState.update {
                    it.copy(
                        isLoading = false, // 로딩 완료
                        profileImageUrl = localProfile.thumbnailUrl,
                        nickname = localProfile.name,
                        birthday = localProfile.birthDay,
                        age = calculatedAge, // 계산된 나이 추가
                        bio = bioSentence,
                        error = null // 또는 기존 에러 유지
                    )
                }

            } catch (e: Exception) { // 로컬 데이터 로딩 실패 시
                println("Error loading profile data from local DB: ${e.message}")
                _myPageState.update {
                    it.copy(
                        isLoading = false,
                        error = "프로필 정보를 불러오는데 실패했습니다."
                    )
                }
            }
        }
    }

    // 생년월일로부터 만나이를 계산하는 private 함수
    private fun calculateAge(birthday: String): Int {
        return try {
            val birthDate = if (birthday.contains("-")) {
                LocalDate.parse(birthday) // YYYY-MM-DD 형식
            } else {
                LocalDate.parse(birthday, java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")) // YYYYMMDD 형식
            }
            val currentDate = LocalDate.now()
            Period.between(birthDate, currentDate).years
        } catch (e: java.time.format.DateTimeParseException) {
            println("Error parsing birthday format '$birthday': ${e.message}")
            0 // 날짜 파싱 에러 시 0 반환
        } catch (e: Exception) {
            println("Error calculating age for birthday '$birthday': ${e.message}")
            0 // 다른 에러 발생 시 0 반환
        }
    }

    // --- 이벤트 핸들러 ---
    fun onLogoutClick() {
        viewModelScope.launch {
            clearTokensUseCase()
            deleteUserProfileUseCase()
            clearMyTagUseCase()
            _logoutCompleted.emit(Unit)
        }
    }
}
