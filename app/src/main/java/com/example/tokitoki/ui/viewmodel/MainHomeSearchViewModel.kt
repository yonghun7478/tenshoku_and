package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.usecase.GetUsersByLoginUseCase
import com.example.tokitoki.domain.usecase.GetUsersBySignupUseCase
import com.example.tokitoki.domain.usecase.ClearCachedUserIdsUseCase
import com.example.tokitoki.ui.converter.UserUiMapper
import com.example.tokitoki.ui.state.MainHomeSearchState
import com.example.tokitoki.ui.state.MainHomeSearchUiEvent
import com.example.tokitoki.ui.state.MainHomeSearchUiState
import com.example.tokitoki.ui.state.MainHomeSearchUiStateData
import com.example.tokitoki.ui.state.OrderType
import com.example.tokitoki.ui.state.currentData
import com.example.tokitoki.ui.state.updateData
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
    private val getUsersBySignupUseCase: GetUsersBySignupUseCase,
    private val clearCachedUserIdsUseCase: ClearCachedUserIdsUseCase
) : ViewModel() {

    // UI 상태(StateFlow)
    private val _uiState = MutableStateFlow(MainHomeSearchUiState())
    val uiState: StateFlow<MainHomeSearchUiState> = _uiState.asStateFlow()

    // UI 이벤트(SharedFlow)
    private val _uiEvent = MutableSharedFlow<MainHomeSearchUiEvent>()
    val uiEvent: SharedFlow<MainHomeSearchUiEvent> = _uiEvent.asSharedFlow()

    // 유저 데이터 로드
    suspend fun fetchUsers(limit: Int = 20, showLoading: Boolean = false) {
        _uiState.update {
            val currentData = it.currentData()
            if (currentData.state == MainHomeSearchState.LOADING || currentData.isLastPage) return
            it.updateData(
                currentData.copy(
                    showShimmerEffect = showLoading,
                    state = MainHomeSearchState.LOADING
                )
            )
        }

        delay(2000)

        val result = if (_uiState.value.orderType == OrderType.LOGIN)
            getUsersByLoginUseCase.execute(_uiState.value.currentData().cursor, limit)
        else
            getUsersBySignupUseCase.execute(_uiState.value.currentData().cursor, limit)

        _uiState.update {
            val currentData = it.currentData() // 최신 값 반영
            when (result) {
                is ResultWrapper.Success -> {
                    val updatedUsers =
                        currentData.users + result.data.users.map(UserUiMapper::domainToUi)

                    it.updateData(
                        currentData.copy(
                            state = MainHomeSearchState.COMPLETED,
                            showShimmerEffect = false,
                            users = updatedUsers,
                            isLastPage = result.data.isLastPage,
                            cursor = result.data.nextCursor
                        )
                    )
                }

                is ResultWrapper.Error -> {
                    it.updateData(currentData.copy(state = MainHomeSearchState.ERROR))
                }

                ResultWrapper.Loading -> TODO()
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
    fun resetState() {
        _uiState.update {
            it.updateData(newData = MainHomeSearchUiStateData())
        }
    }

    fun changeOrderType(curOrderType: OrderType) {
        _uiState.update {
            it.copy(
                orderType = curOrderType
            )
        }
    }

    suspend fun onPullToRefreshTrigger() {
        _uiState.update { it.updateData(it.currentData().copy(isRefreshing = true)) }
        
        // 현재 정렬 순서에 따라 캐시 초기화
        val orderBy = if (_uiState.value.orderType == OrderType.LOGIN) "lastLoginAt" else "createdAt"
        clearCachedUserIdsUseCase(orderBy)
        
        fetchUsers(showLoading = true)
        _uiState.update { it.updateData(it.currentData().copy(isRefreshing = false)) }
    }
}
