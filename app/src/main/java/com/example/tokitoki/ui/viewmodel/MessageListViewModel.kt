package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.model.MatchingUser
import com.example.tokitoki.domain.model.PreviousChat
import com.example.tokitoki.ui.state.MessageListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MessageListViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MessageListUiState(emptyList(), emptyList()))
    val uiState: StateFlow<MessageListUiState> = _uiState

    init {
        // ViewModel 초기화 시에 데이터 로딩 시작
        loadMessageList()
    }

    // 5. 데이터 로딩 함수 (현재는 더미 데이터 사용)
    fun loadMessageList() {
        // 로딩 상태 시작
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            // 실제 UseCase 호출은 여기에서 이루어집니다. (아직 UseCase가 없으므로 더미 데이터 사용)
            try {
                // Simulate network delay
                kotlinx.coroutines.delay(500)

                // 더미 데이터 생성
                val dummyMatchingUsers = listOf(
                    MatchingUser(
                        "1",
                        "Alice",
                        "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg"
                    ),
                    MatchingUser(
                        "2",
                        "Bob",
                        "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg"
                    ),
                    MatchingUser(
                        "3",
                        "Charlie",
                        "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg"
                    )
                )
                val dummyPreviousChats = listOf(
                    PreviousChat(
                        "4",
                        "David",
                        "Seoul",
                        java.time.LocalDate.of(2024, 1, 20),
                        "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg"
                    ),
                    PreviousChat(
                        "5",
                        "Eve",
                        "Busan",
                        LocalDate.of(2024, 1, 15),
                        "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg"
                    ),
                    PreviousChat(
                        "6",
                        "Frank",
                        "Tokyo",
                        LocalDate.of(2024, 1, 10),
                        "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg"
                    ),
                    PreviousChat(
                        "7",
                        "Grace",
                        "Osaka",
                        LocalDate.of(2024, 2, 1),
                        "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg"
                    )
                )

                // 상태 업데이트
                _uiState.value = MessageListUiState(
                    matchingUsers = dummyMatchingUsers,
                    previousChats = dummyPreviousChats,
                    isLoading = false, // 로딩 완료
                    errorMessage = null
                )
            } catch (e: Exception) {
                // 에러 처리
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "An error occurred"
                )
            }
        }
    }
}
