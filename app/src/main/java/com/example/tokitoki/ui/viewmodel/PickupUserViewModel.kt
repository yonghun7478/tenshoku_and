package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.usecase.AddUserIdsToCacheUseCase
import com.example.tokitoki.domain.usecase.FetchPickupUsersUseCase
import com.example.tokitoki.domain.usecase.LikeUserUseCase
import com.example.tokitoki.ui.converter.PickupUserMapper
import com.example.tokitoki.ui.screen.CardDirection
import com.example.tokitoki.ui.state.PickupUserState
import com.example.tokitoki.ui.state.PickupUserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PickupUserViewModel @Inject constructor(
    private val fetchPickupUsersUseCase: FetchPickupUsersUseCase,
    private val addUserIdsToCacheUseCase: AddUserIdsToCacheUseCase,
    private val likeUserUseCase: LikeUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PickupUserUiState())
    val uiState: StateFlow<PickupUserUiState> = _uiState

    init {
        loadPickupUsers()
    }

    fun loadPickupUsers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                state = PickupUserState.LOADING
            )

            when (val result = fetchPickupUsersUseCase()) {
                is ResultWrapper.Success -> {
                    _uiState.value = _uiState.value.copy(
                        users = result.data.map { PickupUserMapper.toPresentation(it) },
                        state = PickupUserState.COMPLETE
                    )
                }
                is ResultWrapper.Error -> {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to load users",
                        state = PickupUserState.COMPLETE
                    )
                }
                ResultWrapper.Loading -> {
                    _uiState.value = _uiState.value.copy(
                        state = PickupUserState.LOADING
                    )
                }
            }
        }
    }

    fun likePickupUser() {
        viewModelScope.launch(Dispatchers.IO) {
            if(_uiState.value.users.isNotEmpty()) {
                val pickupUserId = _uiState.value.users.first().id
                when (likeUserUseCase.invoke(pickupUserId)) {
                    is ResultWrapper.Success -> {
                        removePickupUser(pickupUserId)
                    }
                    is ResultWrapper.Error -> {
                        // 에러 처리: 필요에 따라 사용자에게 메시지 표시 또는 로깅
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "좋아요 실패",
                            state = PickupUserState.COMPLETE
                        )
                    }
                    ResultWrapper.Loading -> {
                        // 로딩 상태 처리 (현재는 아무것도 하지 않음)
                    }
                }
            }
        }
    }

    fun dislikePickupUser() {
        viewModelScope.launch(Dispatchers.IO) {
            if(_uiState.value.users.isNotEmpty()) {
                val pickupUserId = _uiState.value.users.first().id
                removePickupUser(pickupUserId)
            }
        }
    }

    private fun removePickupUser(pickupUserId: String) {
        _uiState.value = _uiState.value.copy(
            users = _uiState.value.users.filterNot { it.id == pickupUserId }
        )
    }

    fun triggerAutoRemove(direction: CardDirection) {
        _uiState.update { currentState ->
            // 리스트가 비어있지 않다면 첫 번째 카드의 autoRemoveDirection 업데이트
            if (currentState.users.isNotEmpty()) {
                currentState.users.first().cardDirection.value = direction
            }
            currentState
        }
    }

    fun onUserClick(userId: String) {
        if(_uiState.value.users.isNotEmpty()) {
            val pickupUserId = _uiState.value.users.first().id
            addUserIdsToCacheUseCase("MainHomePickupScreen", listOf(pickupUserId))
        }
    }
}