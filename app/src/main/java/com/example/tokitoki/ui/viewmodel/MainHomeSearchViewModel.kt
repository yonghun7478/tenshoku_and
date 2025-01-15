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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
    private var loginCursor: String? = null
    private var registCursor: String? = null

    // 유저 데이터 로드
    suspend fun fetchUsers(limit: Int = 20) {
        if (_uiState.value.state == MainHomeSearchState.LOADING || _uiState.value.isLastPage) return

        val orderType = _uiState.value.orderType

        // 로딩 상태 업데이트
        _uiState.update {
            it.copy(
                state = MainHomeSearchState.LOADING
            )
        }

        delay(1500)

        // 유스케이스 선택 및 호출
        val result = when (orderType) {
            OrderType.LOGIN -> getUsersByLoginUseCase.execute(loginCursor, limit)
            OrderType.REGISTRATION -> getUsersBySignupUseCase.execute(registCursor, limit)
        }

        // 결과 처리
        when (result) {
            is ResultWrapper.Success -> {

                if (orderType == OrderType.LOGIN) {
                    val updatedUsers =
                        _uiState.value.usersOrderByLogin + result.data.users.map { user ->
                            UserUiMapper.domainToUi(user)
                        }

                    loginCursor = result.data.nextCursor

                    _uiState.update {
                        it.copy(
                            state = if (updatedUsers.isEmpty()) MainHomeSearchState.NOTHING else MainHomeSearchState.COMPLETED,
                            usersOrderByLogin = updatedUsers,
                            isLastPage = result.data.isLastPage
                        )
                    }
                } else {
                    val updatedUsers =
                        _uiState.value.usersOrderByRegist + result.data.users.map { user ->
                            UserUiMapper.domainToUi(user)
                        }

                    registCursor = result.data.nextCursor

                    _uiState.update {
                        it.copy(
                            state = if (updatedUsers.isEmpty()) MainHomeSearchState.NOTHING else MainHomeSearchState.COMPLETED,
                            usersOrderByRegist = updatedUsers,
                            isLastPage = result.data.isLastPage
                        )
                    }
                }
            }

            is ResultWrapper.Error -> {
                // 에러 처리
                _uiState.update {
                    it.copy(
                        state = MainHomeSearchState.NOTHING
                    )
                }
                _uiEvent.emit(MainHomeSearchUiEvent.Error(result.errorType))
            }
        }

    }

    // UI 이벤트 처리
    fun onEvent(event: MainHomeSearchUiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    // 상태 초기화
    fun resetState(orderType: OrderType) {
        _uiState.update {
            it.copy(
                orderType = orderType,
                state = MainHomeSearchState.NOTHING,
                isLastPage = false
            )
        }
    }

    suspend fun onPullToRefreshTrigger() {
        _uiState.update {
            it.copy(
                isRefreshing = true
            )
        }

        fetchUsers()

        _uiState.update {
            it.copy(
                isRefreshing = false
            )
        }
    }
}
