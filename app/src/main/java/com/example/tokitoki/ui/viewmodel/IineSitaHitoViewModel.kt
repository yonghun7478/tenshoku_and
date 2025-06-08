package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.repository.LikeRepository
import com.example.tokitoki.domain.usecase.GetLikesUseCase
import com.example.tokitoki.domain.usecase.AddUserIdsToCacheUseCase
import com.example.tokitoki.ui.state.IineSitaHitoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IineSitaHitoViewModel @Inject constructor(
    private val getLikesUseCase: GetLikesUseCase,
    private val addUserIdsToCacheUseCase: AddUserIdsToCacheUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(IineSitaHitoUiState())
    val uiState: StateFlow<IineSitaHitoUiState> = _uiState.asStateFlow()

    private var currentCursor: Long? = null
    private val pageSize = 10

    init {
        loadInitialUsers()
    }

    private fun loadInitialUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val result = getLikesUseCase(LikeRepository.SENT, null, pageSize)
                result.getOrNull()?.let { likeResult ->
                    currentCursor = likeResult.nextCursor
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            users = likeResult.likes,
                            hasMoreItems = likeResult.nextCursor != null
                        )
                    }
                } ?: run {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "데이터를 불러오는데 실패했습니다."
                        )
                    }
                }
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = error.message ?: "알 수 없는 오류가 발생했습니다."
                    )
                }
            }
        }
    }

    fun loadMoreUsers() {
        if (_uiState.value.isLoading || !_uiState.value.hasMoreItems) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val result = getLikesUseCase(LikeRepository.SENT, currentCursor, pageSize)
                result.getOrNull()?.let { likeResult ->
                    currentCursor = likeResult.nextCursor
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            users = it.users + likeResult.likes,
                            hasMoreItems = likeResult.nextCursor != null
                        )
                    }
                } ?: run {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "데이터를 불러오는데 실패했습니다."
                        )
                    }
                }
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = error.message ?: "알 수 없는 오류가 발생했습니다."
                    )
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { 
                IineSitaHitoUiState(
                    users = emptyList(),
                    isLoading = true,
                    isRefreshing = true,
                    hasMoreItems = false,
                    error = null
                )
            }
            try {
                currentCursor = null
                val result = getLikesUseCase(LikeRepository.SENT, null, pageSize)
                result.getOrNull()?.let { likeResult ->
                    currentCursor = likeResult.nextCursor
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            isLoading = false,
                            users = likeResult.likes,
                            hasMoreItems = likeResult.nextCursor != null
                        )
                    }
                } ?: run {
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            isLoading = false,
                            error = "데이터를 불러오는데 실패했습니다."
                        )
                    }
                }
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        isLoading = false,
                        error = error.message ?: "알 수 없는 오류가 발생했습니다."
                    )
                }
            }
        }
    }

    fun onUserClick(userId: String) {
        // 현재 화면의 모든 사용자 ID를 캐시에 저장
        val userIds = _uiState.value.users.map { it.id }
        addUserIdsToCacheUseCase("IineSitaHitoScreen", userIds)
    }
} 