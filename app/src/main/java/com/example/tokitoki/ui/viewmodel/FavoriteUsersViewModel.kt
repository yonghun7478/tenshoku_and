package com.example.tokitoki.ui.viewmodel

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.usecase.GetFavoriteUsersUseCase
import com.example.tokitoki.domain.usecase.SendMitenUseCase
import com.example.tokitoki.domain.usecase.AddUserIdsToCacheUseCase
import com.example.tokitoki.ui.state.FavoriteUsersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteUsersViewModel @Inject constructor(
    private val getFavoriteUsersUseCase: GetFavoriteUsersUseCase,
    private val sendMitenUseCase: SendMitenUseCase,
    private val addUserIdsToCacheUseCase: AddUserIdsToCacheUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteUsersUiState())
    val uiState: StateFlow<FavoriteUsersUiState> = _uiState.asStateFlow()

    private var cursor: Long = 0 // 초기 커서 값
    private val limit: Int = 10 // 한 번에 가져올 사용자 수

    init {
        loadFavoriteUsers()
    }

    fun loadFavoriteUsers() {
        if (_uiState.value.isLoading || !_uiState.value.hasMore) return // 이미 로딩 중이거나 더 이상 데이터가 없으면 무시

        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            delay(1000)
            val result = getFavoriteUsersUseCase(limit, cursor)
            if (result.isNotEmpty()) {
                _uiState.value = _uiState.value.copy(
                    favoriteUsers = _uiState.value.favoriteUsers + result,
                    isLoading = false,
                    isRefreshing = false,
                    hasMore = result.size == limit // 결과가 limit과 같으면 다음 페이지가 있을 수 있음
                )
                cursor = result.lastOrNull()?.timestamp ?: cursor // 다음 커서 업데이트
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    hasMore = false // 더 이상 데이터가 없음
                )
            }
        }
    }

    fun refreshFavoriteUsers() {
        cursor = 0 // 커서 초기화
        _uiState.value = FavoriteUsersUiState(isRefreshing = true) // 상태 초기화
        loadFavoriteUsers()
    }

    fun sendMiten(userId: String, isCurrentlySending: Boolean) {
        viewModelScope.launch {
            if (isCurrentlySending) return@launch // Prevent multiple calls for the same user

            // Mark the specific user as sending
            _uiState.update { currentState ->
                val updatedUsers = currentState.favoriteUsers.map { user ->
                    if (user.id == userId) {
                        user.copy(isSendingMiten = true)
                    } else {
                        user
                    }
                }
                currentState.copy(favoriteUsers = updatedUsers)
            }

            when (val result = sendMitenUseCase(userId)) {
                is ResultWrapper.Success -> {
                    _uiState.update { currentState ->
                        val newUsers = currentState.favoriteUsers.map { user ->
                            if (user.id == userId) {
                                user.copy(isSendingMiten = false)
                            } else {
                                user
                            }
                        }
                        currentState.copy(toastMessage = "みてねを送信しました", favoriteUsers = newUsers)
                    }
                }
                is ResultWrapper.Error -> {
                    _uiState.update { currentState ->
                        val newUsers = currentState.favoriteUsers.map { user ->
                            if (user.id == userId) {
                                user.copy(isSendingMiten = false)
                            } else {
                                user
                            }
                        }
                        currentState.copy(toastMessage = "送信に失敗しました", favoriteUsers = newUsers)
                    }
                }
                ResultWrapper.Loading -> {
                    _uiState.update { currentState ->
                        val newUsers = currentState.favoriteUsers.map { user ->
                            if (user.id == userId) {
                                user.copy(isSendingMiten = false)
                            } else {
                                user
                            }
                        }
                        currentState.copy(favoriteUsers = newUsers, toastMessage = "予期せぬ状態: 送信処理中...")
                    }
                }
            }
        }
    }

    fun clearToastMessage() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun onUserClick(userId: String) {
        // 현재 화면의 모든 사용자 ID를 캐시에 저장
        val userIds = _uiState.value.favoriteUsers.map { it.id }
        addUserIdsToCacheUseCase("FavoriteUsersScreen", userIds)
    }

    // 필요한 추가 인터랙션 (예: 사용자 상세 화면 이동)은 여기에 추가
}