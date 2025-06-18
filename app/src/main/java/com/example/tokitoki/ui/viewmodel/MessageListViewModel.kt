package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.tokitoki.domain.usecase.GetMatchingUsersUseCase
import com.example.tokitoki.domain.usecase.GetPreviousChatsUseCase
import com.example.tokitoki.domain.usecase.AddUserIdsToCacheUseCase
import kotlinx.coroutines.flow.asStateFlow
import com.example.tokitoki.ui.state.MessageListUiState

@HiltViewModel
class MessageListViewModel @Inject constructor(
    private val getMatchingUsersUseCase: GetMatchingUsersUseCase,
    private val getPreviousChatsUseCase: GetPreviousChatsUseCase,
    private val addUserIdsToCacheUseCase: AddUserIdsToCacheUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessageListUiState()) // 초기 상태
    val uiState: StateFlow<MessageListUiState> = _uiState.asStateFlow()

    // 페이징 상태 관리 (간단한 예시)
    private var nextMatchingCursor: String? = null
    private var isMatchingLoading = false
    private var nextPreviousChatCursor: String? = null
    private var isPreviousChatLoading = false

    init {
        viewModelScope.launch {
            loadInitialData()
        }
    }

    // 초기 데이터 로딩 (매칭 유저 & 이전 대화 첫 페이지)
    suspend fun loadInitialData() {
        // 이미 로딩 중이면 중복 실행 방지
        if (_uiState.value.isLoading) return

        _uiState.update { it.copy(isLoading = true, errorMessage = null) } // 전체 로딩 상태 시작

        // 두 API를 동시에 호출 (병렬 처리)
        val matchingResultDeferred = viewModelScope.launch { loadMatchingUsersInternal(null) }
        val previousChatResultDeferred = viewModelScope.launch { loadPreviousChatsInternal(null) }

        // 두 작업이 모두 끝날 때까지 기다림
        matchingResultDeferred.join()
        previousChatResultDeferred.join()

        // 모든 로딩 완료 후 전체 로딩 상태 해제
        _uiState.update { it.copy(isLoading = false) }
        // 개별 로딩 실패 시 에러 메시지는 각 함수 내부에서 처리됨
    }

    // PullToRefreshBox에서 호출되는 리프레시 함수
    suspend fun refreshData() {
        // NOTE: isRefreshing 상태는 Composable 함수 내에서 관리하므로, 여기서는 갱신 로직만 처리합니다.

        // 현재 상태를 초기화하고 다시 로드합니다.
        nextMatchingCursor = null
        nextPreviousChatCursor = null
        _uiState.update {
            MessageListUiState() // 초기 상태로 리셋
        }

        // 데이터 다시 로드
        loadInitialData()
    }

    // 화면이 다시 활성화될 때 UI 갱신 (시머 이펙트 없이)
    fun refreshOnResume() {
        viewModelScope.launch {
            // 커서와 리스트를 초기화하고 첫 페이지만 다시 로드
            nextMatchingCursor = null
            nextPreviousChatCursor = null
            // _uiState.update { it.copy(matchingUsers = emptyList(), previousChats = emptyList()) } // 깜빡임의 원인이므로 제거

            // isLoading 상태를 true로 바꾸지 않고 데이터만 다시 로드
            val matchingResultDeferred = viewModelScope.launch { loadMatchingUsersInternal(null) }
            val previousChatResultDeferred = viewModelScope.launch { loadPreviousChatsInternal(null) }

            matchingResultDeferred.join()
            previousChatResultDeferred.join()
        }
    }

    // 다음 매칭 유저 페이지 로드 (UI에서 호출)
    fun loadMoreMatchingUsers() {
        if (isMatchingLoading || nextMatchingCursor == null) return // 로딩 중이거나 다음 페이지 없으면 리턴
        isMatchingLoading = true;
        viewModelScope.launch {
            loadMatchingUsersInternal(nextMatchingCursor)
        }
    }

    // 다음 이전 대화 페이지 로드 (UI에서 호출)
    fun loadMorePreviousChats() {
        if (isPreviousChatLoading || nextPreviousChatCursor == null) return // 로딩 중이거나 다음 페이지 없으면 리턴
        isPreviousChatLoading = true;
        viewModelScope.launch {
            loadPreviousChatsInternal(nextPreviousChatCursor)
        }
    }


    // 매칭 유저 로딩 내부 로직
    private suspend fun loadMatchingUsersInternal(cursor: String?) {
        // isMatchingLoading = true // 함수 레벨로 이동
        // 만약 _uiState.value.isLoading이 전체 로딩만을 의미한다면, 개별 로딩 상태 표시 필요
        // _uiState.update { it.copy(isMatchingSectionLoading = true) }

        val result = getMatchingUsersUseCase(cursor) // UseCase 호출

        result.onSuccess { cursorResult ->
            nextMatchingCursor = cursorResult.nextCursor // 다음 커서 저장
            _uiState.update { currentState ->
                val updatedList = if (cursor == null) { // 첫 페이지면 교체
                    cursorResult.data
                } else { // 다음 페이지면 기존 리스트에 추가
                    currentState.matchingUsers + cursorResult.data
                }
                currentState.copy(
                    matchingUsers = updatedList,
                    // isMatchingSectionLoading = false
                )
            }
        }.onFailure { throwable ->
            _uiState.update {
                it.copy(
                    errorMessage = throwable.message ?: "Failed to load matching users",
                    // isMatchingSectionLoading = false
                )
            }
        }.also {
            isMatchingLoading = false
        }
        // isMatchingLoading = false //  finally 블록 제거
    }

    // 이전 대화 로딩 내부 로직
    private suspend fun loadPreviousChatsInternal(cursor: String?) {
        // isPreviousChatLoading = true // 함수 레벨로 이동
        // _uiState.update { it.copy(isPreviousChatSectionLoading = true) }

        val result = getPreviousChatsUseCase(cursor) // UseCase 호출

        result.onSuccess { cursorResult ->
            nextPreviousChatCursor = cursorResult.nextCursor // 다음 커서 저장
            _uiState.update { currentState ->
                val updatedList = if (cursor == null) { // 첫 페이지면 교체
                    cursorResult.data
                } else { // 다음 페이지면 기존 리스트에 추가
                    currentState.previousChats + cursorResult.data
                }
                currentState.copy(
                    previousChats = updatedList,
                    // isPreviousChatSectionLoading = false
                )
            }
        }.onFailure { throwable ->
            _uiState.update {
                it.copy(
                    errorMessage = throwable.message ?: "Failed to load previous chats",
                    // isPreviousChatSectionLoading = false
                )
            }
        }.also {
            isPreviousChatLoading = false
        }
        // isPreviousChatLoading = false // finally 블록 제거
    }

    fun addUserIdsToCache(userId: String) {
        addUserIdsToCacheUseCase("MessageListScreen", listOf(userId))
    }
}
