package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.usecase.AddUserIdsToCacheUseCase
import com.example.tokitoki.domain.usecase.DislikePickupUserUseCase
import com.example.tokitoki.domain.usecase.FetchPickupUsersUseCase
import com.example.tokitoki.domain.usecase.LikePickupUserUseCase
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
    private val likePickupUserUseCase: LikePickupUserUseCase,
    private val dislikePickupUserUseCase: DislikePickupUserUseCase,
    private val addUserIdsToCacheUseCase: AddUserIdsToCacheUseCase
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
                likePickupUserUseCase(pickupUserId)
                removePickupUser(pickupUserId)
            }
        }
    }

    fun dislikePickupUser() {
        viewModelScope.launch(Dispatchers.IO) {
            if(_uiState.value.users.isNotEmpty()) {
                val pickupUserId = _uiState.value.users.first().id
                dislikePickupUserUseCase(pickupUserId)
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
        // 현재 화면의 모든 사용자 ID를 캐시에 저장
        val userIds = _uiState.value.users.map { it.id }
        addUserIdsToCacheUseCase("MainHomePickupScreen", userIds)
    }
}