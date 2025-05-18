package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.model.LikedUser
import com.example.tokitoki.domain.usecase.GetLikedUsersUseCase
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
    private val getLikedUsersUseCase: GetLikedUsersUseCase
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
                val users = getLikedUsersUseCase(cursor = null, pageSize = pageSize)
                currentCursor = users.lastOrNull()?.likedAt
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        users = users,
                        hasMoreItems = users.size >= pageSize
                    )
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
                val newUsers = getLikedUsersUseCase(cursor = currentCursor, pageSize = pageSize)
                currentCursor = newUsers.lastOrNull()?.likedAt
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        users = it.users + newUsers,
                        hasMoreItems = newUsers.size >= pageSize
                    )
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
                val users = getLikedUsersUseCase(cursor = null, pageSize = pageSize)
                currentCursor = users.lastOrNull()?.likedAt
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        isLoading = false,
                        users = users,
                        hasMoreItems = users.size >= pageSize
                    )
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
} 