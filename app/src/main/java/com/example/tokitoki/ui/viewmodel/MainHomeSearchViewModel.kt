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
    private var cursor: String? = null

    // 유저 데이터 로드
    suspend fun fetchUsers(limit: Int = 20, showLoading: Boolean = false) {
        if (_uiState.value.dataByLogin.state == MainHomeSearchState.LOADING || _uiState.value.dataByLogin.isLastPage) return

        // 로딩 상태 업데이트
        if (showLoading) {
            _uiState.update {
                it.copy(
                    dataByLogin = it.dataByLogin.copy(
                        state = MainHomeSearchState.LOADING
                    ),
                )
            }
        }

        delay(2000)

        // 유스케이스 선택 및 호출
        val result = getUsersByLoginUseCase.execute(cursor, limit)

        // 결과 처리
        when (result) {
            is ResultWrapper.Success -> {
                val updatedUsers =
                    _uiState.value.dataByLogin.users + result.data.users.map { user ->
                        UserUiMapper.domainToUi(user)
                    }

                cursor = result.data.nextCursor

                _uiState.update {
                    it.copy(
                        dataByLogin = it.dataByLogin.copy(
                            state = MainHomeSearchState.COMPLETED,
                            users = updatedUsers,
                            isLastPage = result.data.isLastPage
                        ),
                    )
                }
            }

            is ResultWrapper.Error -> {
                // 에러 처리
                _uiState.update {
                    it.copy(
                        dataByLogin = it.dataByLogin.copy(
                            state = MainHomeSearchState.ERROR
                        ),
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
    fun resetState() {
        _uiState.value = MainHomeSearchUiState()
    }

    suspend fun onPullToRefreshTrigger() {
        _uiState.update {
            it.copy(
                dataByLogin = it.dataByLogin.copy(
                    isRefreshing = true
                ),
            )
        }

        fetchUsers(showLoading = true)

        _uiState.update {
            it.copy(
                dataByLogin = it.dataByLogin.copy(
                    isRefreshing = false
                ),
            )
        }
    }
}
