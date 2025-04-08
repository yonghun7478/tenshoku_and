package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.FetchMyProfileUseCase
import com.example.tokitoki.domain.usecase.GetMyProfileUseCase
import com.example.tokitoki.domain.usecase.GetMySelfSentenceUseCase
import com.example.tokitoki.domain.usecase.SetMyProfileUseCase
import com.example.tokitoki.ui.state.MyPageDummyData
import com.example.tokitoki.ui.state.MyPageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel
@HiltViewModel
class MyPageViewModel @Inject constructor(
    // UseCase 의존성 변경: Fetch와 Save 분리
    private val fetchMyProfileUseCase: FetchMyProfileUseCase, // 신규 Fetch UseCase
    private val setMyProfileUseCase: SetMyProfileUseCase,     // 신규 Save UseCase
    private val getMyProfileUseCase: GetMyProfileUseCase,     // 기존 Local Get UseCase
    private val getMySelfSentenceUseCase: GetMySelfSentenceUseCase // 기존 Sentence Get UseCase
) : ViewModel() {

    private val _myPageState = MutableStateFlow(MyPageState())
    val myPageState: StateFlow<MyPageState> = _myPageState.asStateFlow()

    init {
        loadMyPageData()
    }

    private fun loadMyPageData() {
        viewModelScope.launch {
            _myPageState.update { it.copy(isLoading = true, error = null) }

            // 1. Fetch UseCase 호출하여 데이터 가져오기 시도
            val remoteProfile = fetchMyProfileUseCase()

            // 2. Fetch 성공 시 (null이 아닐 경우) Save UseCase 호출하여 저장
            if (remoteProfile != null) {
                try {
                    setMyProfileUseCase(remoteProfile)
                } catch (e: Exception) {
                    // 저장 중 에러 발생 시 (선택적 처리)
                    println("Error saving profile after fetch: ${e.message}")
                    // 에러 상태를 업데이트 할 수도 있음
                    // _myPageState.update { it.copy(error = "프로필 저장 중 오류 발생") }
                }
            } else {
                // Fetch 실패 시 (선택적 처리)
                println("Failed to fetch remote profile.")
                // 에러 상태를 업데이트 할 수도 있음
                // _myPageState.update { it.copy(error = "최신 프로필 로딩 실패") }
            }

            // 3. 로컬 DB에서 프로필 정보 다시 로드 (Fetch/Save 결과 반영)
            try {
                val localProfile = getMyProfileUseCase() // 로컬 프로필 가져오기

                var bioSentence: String? = null
                if (localProfile.mySelfSentenceId > 0) {
                    try {
                        val selfSentence = getMySelfSentenceUseCase(localProfile.mySelfSentenceId)
                        bioSentence = selfSentence.sentence
                    } catch (e: Exception) {
                        println("Error fetching self sentence (id: ${localProfile.mySelfSentenceId}): ${e.message}")
                    }
                }

                // 4. 최종 UI State 업데이트
                _myPageState.update {
                    it.copy(
                        isLoading = false, // 로딩 완료
                        profileImageUrl = localProfile.thumbnailUrl,
                        nickname = localProfile.name,
                        bio = bioSentence,
                        // Fetch/Save 실패 시 남겨둔 에러 메시지가 있다면 여기서 null로 덮어쓰지 않도록 주의
                        // error = if (it.error != null) it.error else null // 기존 에러 유지 또는 null
                        error = null // 또는 항상 null로 초기화
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

    // --- 이벤트 핸들러 ---
    fun onEditProfileClick() { println("ViewModel: 프로필 수정 클릭") }
    fun onSeenMeClick() { println("ViewModel: 나를 본 사람 클릭") }
    fun onFavoritesClick() { println("ViewModel: 즐겨찾기 클릭") }
    fun onLikedMeClick() { println("ViewModel: 내가 좋아요 한 사람 클릭") }
    fun onLogoutClick() { println("ViewModel: 로그아웃 클릭") }
}
