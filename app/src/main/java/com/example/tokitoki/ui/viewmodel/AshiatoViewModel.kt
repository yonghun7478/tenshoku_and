package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.model.AshiatoTimeline
import com.example.tokitoki.domain.usecase.GetAshiatoPageUseCase
import com.example.tokitoki.ui.state.AshiatoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AshiatoViewModel @Inject constructor(
    private val getAshiatoPageUseCase: GetAshiatoPageUseCase // UseCase 주입
) : ViewModel() {

    // UI 상태를 관리하는 StateFlow
    private val _uiState = MutableStateFlow(AshiatoUiState())
    val uiState: StateFlow<AshiatoUiState> = _uiState.asStateFlow()

    // 다음 페이지 로드를 위한 커서 값 저장
    private var nextCursor: String? = null
    // 현재 로딩 중인지 여부를 판단하는 플래그 (중복 로딩 방지)
    private var isCurrentlyLoading = false

    init {
        // ViewModel 생성 시 초기 데이터 로드
        loadInitialData()
    }

    /**
     * 초기 데이터를 로드합니다.
     */
    private fun loadInitialData() {
        // 이미 로딩 중이거나 초기 로딩이 완료된 상태면 실행하지 않음 (선택적)
        if (isCurrentlyLoading || !_uiState.value.isLoadingInitial) return
        fetchAshiatoData(cursorToFetch = null, isInitialLoad = true)
    }

    /**
     * 데이터를 새로고침합니다. (Pull-to-Refresh)
     */
    fun refresh() {
        // 이미 로딩 중이면 실행하지 않음
        if (isCurrentlyLoading) return
        // 새로고침 시에는 커서를 초기화해야 함
        nextCursor = null
        fetchAshiatoData(cursorToFetch = null, isRefresh = true)
    }

    /**
     * 다음 페이지 데이터를 로드합니다. (무한 스크롤)
     */
    fun loadMore() {
        // 이미 로딩 중이거나, 더 로드할 수 없는 상태(nextCursor가 null)면 실행하지 않음
        if (isCurrentlyLoading || nextCursor == null) return
        // isLoadingMore 파라미터 제거됨
        fetchAshiatoData(cursorToFetch = nextCursor)
    }

    /**
     * UseCase를 호출하여 아시아토 데이터를 가져오는 내부 함수
     * @param cursorToFetch 요청할 페이지의 커서 (null이면 첫 페이지)
     * @param isInitialLoad 초기 로딩인지 여부
     * @param isRefresh 새로고침인지 여부
     */
    private fun fetchAshiatoData(
        cursorToFetch: String?,
        isInitialLoad: Boolean = false,
        isRefresh: Boolean = false
        // isLoadingMore 파라미터 제거됨
    ) {
        // 중복 로딩 방지
        if (isCurrentlyLoading) return
        isCurrentlyLoading = true

        // 로딩 상태 UI 업데이트
        _uiState.update { currentState ->
            currentState.copy(
                isLoadingInitial = isInitialLoad, // isLoading으로 변경 예정
                isRefreshing = isRefresh,
                // isLoadingMore 제거됨
                error = null // 로딩 시작 시 이전 오류 초기화
            )
        }

        // viewModelScope에서 UseCase 실행
        viewModelScope.launch {
            getAshiatoPageUseCase(cursor = cursorToFetch)
                .onSuccess { resultPage ->
                    // 성공 시 UI 상태 업데이트
                    _uiState.update { currentState ->
                        // 새로고침 또는 초기 로드 시 기존 목록 대체, 추가 로드 시 이어붙이기
                        val newLogs = if (isInitialLoad || isRefresh) {
                            resultPage.logs
                        } else {
                            // 추가 로드 시에는 현재 timeline의 dailyLogs에 이어붙임
                            currentState.timeline.dailyLogs + resultPage.logs
                        }
                        // 다음 커서 업데이트
                        nextCursor = resultPage.nextCursor

                        currentState.copy(
                            isLoadingInitial = false, // isLoading으로 변경 예정
                            isRefreshing = false,
                            // isLoadingMore 제거됨
                            timeline = AshiatoTimeline(dailyLogs = newLogs),
                            canLoadMore = resultPage.nextCursor != null,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    // 실패 시 UI 상태 업데이트
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoadingInitial = false, // isLoading으로 변경 예정
                            isRefreshing = false,
                            // isLoadingMore 제거됨
                            error = error // 오류 정보 전달
                        )
                    }
                    // 실패 로그 출력 (실제 앱에서는 Crashlytics 등 사용)
                    println("Error fetching Ashiato data: $error")
                }

            // 로딩 완료 상태로 변경
            isCurrentlyLoading = false
        }
    }
}