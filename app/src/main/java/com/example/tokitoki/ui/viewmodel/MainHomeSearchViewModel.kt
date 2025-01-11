package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.usecase.GetUsersByLoginUseCase
import com.example.tokitoki.domain.usecase.GetUsersBySignupUseCase
import com.example.tokitoki.ui.converter.UserUiMapper
import com.example.tokitoki.ui.state.MainHomeSearchState
import com.example.tokitoki.ui.state.MainHomeSearchUiEvent
import com.example.tokitoki.ui.state.MainHomeSearchUiState
import com.example.tokitoki.ui.state.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainHomeSearchViewModel @Inject constructor(
    private val getUsersByLoginUseCase: GetUsersByLoginUseCase,
    private val getUsersBySignupUseCase: GetUsersBySignupUseCase
) : ViewModel() {

    // UI 상태(StateFlow)
    private val _uiState = MutableStateFlow(MainHomeSearchUiState())
    val uiState: StateFlow<MainHomeSearchUiState> = _uiState.asStateFlow()

    // UI 이벤트(SharedFlow)
    private val _uiEvent = MutableSharedFlow<MainHomeSearchUiEvent>()
    val uiEvent: SharedFlow<MainHomeSearchUiEvent> = _uiEvent.asSharedFlow()

    // 커서 상태
    private var currentCursor: String? = null

    // 유저 데이터 로드
    fun fetchUsers(limit: Int = 20) {
        val orderType = _uiState.value.orderType

        viewModelScope.launch {
            // 로딩 상태 업데이트
            _uiState.value = _uiState.value.copy(state = MainHomeSearchState.LOADING)

            // 유스케이스 선택 및 호출
            val result = when (orderType) {
                OrderType.LOGIN -> getUsersByLoginUseCase.execute(currentCursor, limit)
                OrderType.REGISTRATION -> getUsersBySignupUseCase.execute(currentCursor, limit)
            }

            // 결과 처리
            when (result) {
                is ResultWrapper.Success -> {
                    val updatedUsers = _uiState.value.users + result.data.users.map { user ->
                        UserUiMapper.domainToUi(user)
                    }
                    currentCursor = result.data.nextCursor // 커서 업데이트
                    _uiState.value = _uiState.value.copy(
                        state = if (updatedUsers.isEmpty()) MainHomeSearchState.NOTHING else MainHomeSearchState.INITIALIZED,
                        users = updatedUsers,
                        isLastPage = result.data.isLastPage
                    )
                }
                is ResultWrapper.Error -> {
                    // 에러 처리
                    _uiState.value = _uiState.value.copy(state = MainHomeSearchState.NOTHING)
                    _uiEvent.emit(MainHomeSearchUiEvent.Error(result.errorType))
                }
            }
        }
    }

    // UI 이벤트 처리
    fun onEvent(event: MainHomeSearchUiEvent) {
        when (event) {
            is MainHomeSearchUiEvent.OrderSelected -> {
                _uiState.value = _uiState.value.copy(orderType = event.orderType)
                resetState()
            }
            is MainHomeSearchUiEvent.UserSelected -> {
                viewModelScope.launch {
                    _uiEvent.emit(event)
                }
            }
            is MainHomeSearchUiEvent.LoadMore -> {
                if (!_uiState.value.isLastPage) fetchUsers(limit = 20)
            }

            is MainHomeSearchUiEvent.Error -> {

            }
        }
    }

    // 상태 초기화
    private fun resetState() {
        currentCursor = null
        _uiState.value = _uiState.value.copy(
            state = MainHomeSearchState.NOTHING,
            users = emptyList(),
            isLastPage = false
        )
    }
}
