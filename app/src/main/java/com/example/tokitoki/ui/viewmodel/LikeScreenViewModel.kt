package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.model.LikeItem
import com.example.tokitoki.domain.model.LikeResult
import com.example.tokitoki.domain.usecase.GetLikesUseCase
import com.example.tokitoki.ui.state.DeleteItemState
import com.example.tokitoki.ui.state.DeleteModeState
import com.example.tokitoki.ui.state.LikeItemUiState
import com.example.tokitoki.ui.state.LikeScreenUiState
import com.example.tokitoki.ui.state.LikeTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LikeScreenViewModel @Inject constructor(
    private val getLikesUseCase: GetLikesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LikeScreenUiState())
    val uiState: StateFlow<LikeScreenUiState> = _uiState.asStateFlow()

    init {
        loadLikes()
    }

    private fun loadLikes() {
        viewModelScope.launch {
            // UseCases를 사용하여 데이터 로드
            handleResult(getLikesUseCase(LikeTab.RECEIVED.title, null), LikeTab.RECEIVED)
            handleResult(getLikesUseCase(LikeTab.SENT.title, null), LikeTab.SENT)
            handleResult(getLikesUseCase(LikeTab.MATCHED.title, null), LikeTab.MATCHED)
        }
    }

    fun refreshLikes() {
        viewModelScope.launch {
            val currentTab = _uiState.value.selectedTab
            when (currentTab) {
                LikeTab.RECEIVED -> {
                    _uiState.update { it.copy(receivedLikesIsRefreshing = true, receivedLikes = emptyList()) }
                    delay(500)
                    handleResult(getLikesUseCase(currentTab.title, null), currentTab) //데이터를 다시 가져온다.
                }
                LikeTab.SENT -> {
                    _uiState.update { it.copy(sentLikesIsRefreshing = true, sentLikes = emptyList()) }
                    delay(500)
                    handleResult(getLikesUseCase(currentTab.title, null), currentTab)
                }
                LikeTab.MATCHED -> {
                    _uiState.update { it.copy(matchedLikesIsRefreshing = true, matchedLikes = emptyList()) }
                    delay(500)
                    handleResult(getLikesUseCase(currentTab.title, null), currentTab)
                }
            }
        }
    }

    fun loadMoreLikes() {
        viewModelScope.launch {
            val currentTab = _uiState.value.selectedTab
            val cursor = when (currentTab) {
                LikeTab.RECEIVED -> _uiState.value.receivedCursor
                LikeTab.SENT -> _uiState.value.sentCursor
                LikeTab.MATCHED -> _uiState.value.matchedCursor
            }

            handleLoadMoreResult(
                getLikesUseCase(currentTab.title, cursor),
                currentTab
            )
        }
    }

    // 단일 아이템 삭제
    fun onConfirmDeleteItem() {
        val itemId = _uiState.value.deleteItemState.itemId ?: return
        _uiState.update { currentState ->
            currentState.copy(
                receivedLikes = currentState.receivedLikes.filterNot { it.id == itemId },
                sentLikes = currentState.sentLikes.filterNot { it.id == itemId },
                matchedLikes = currentState.matchedLikes.filterNot { it.id == itemId },
                showSnackBar = true,
                deleteItemState = DeleteItemState()
            )
        }
    }

    // 선택된 아이템들 삭제
    fun onConfirmDeleteSelectedItems() {
        val selectedItems = _uiState.value.deleteModeState.selectedItems
        _uiState.update { currentState ->
            currentState.copy(
                receivedLikes = currentState.receivedLikes.filterNot { it.id in selectedItems },
                sentLikes = currentState.sentLikes.filterNot { it.id in selectedItems },
                matchedLikes = currentState.matchedLikes.filterNot { it.id in selectedItems },
                deleteModeState = DeleteModeState(), // 삭제 모드 상태 초기화
                showSnackBar = true
            )
        }
    }

    // Event handling functions (called from UI)
    fun onTabSelected(tab: LikeTab) {
        _uiState.update {
            it.copy(
                selectedTab = tab,
                deleteModeState = DeleteModeState() // 탭 변경 시 삭제 모드 상태 초기화
            )
        }
    }

    fun onToggleDeleteMode() {
        _uiState.update {
            it.copy(
                deleteModeState = it.deleteModeState.copy(
                    isDeleteMode = !it.deleteModeState.isDeleteMode,
                    selectedItems = emptySet() // 삭제 모드 전환 시 선택 해제
                )
            )
        }
    }

    fun onItemLongClicked(itemId: String) {
        if (!uiState.value.deleteModeState.isDeleteMode) {
            _uiState.update {
                it.copy(deleteItemState = DeleteItemState(itemId = itemId, showDialog = true))
            }
        }
    }

    //다이얼로그 닫기
    fun onDismissDeleteDialog() {
        _uiState.update {
            it.copy(
                deleteItemState = DeleteItemState(), // 둘 다 초기화
                deleteModeState = it.deleteModeState.copy(showDialog = false)
            )
        }
    }

    fun onDismissSnackBar() {
        _uiState.value = _uiState.value.copy(showSnackBar = false)
    }

    fun onSelectItem(itemId: String) {
        val currentSelected = _uiState.value.deleteModeState.selectedItems.toMutableSet()
        if (currentSelected.contains(itemId)) {
            currentSelected.remove(itemId)
        } else {
            currentSelected.add(itemId)
        }
        _uiState.update {
            it.copy(deleteModeState = it.deleteModeState.copy(selectedItems = currentSelected))
        }
    }

    fun onSelectAllItems() {
        val currentTab = _uiState.value.selectedTab
        val allItemsInCurrentTab = when (currentTab) {
            LikeTab.RECEIVED -> _uiState.value.receivedLikes
            LikeTab.SENT -> _uiState.value.sentLikes
            LikeTab.MATCHED -> _uiState.value.matchedLikes
        }.map { it.id }

        val currentSelected = _uiState.value.deleteModeState.selectedItems.toMutableSet()
        val areAllSelected = currentSelected.containsAll(allItemsInCurrentTab)

        if (areAllSelected) {
            // If all are selected, deselect all
            currentSelected.removeAll(allItemsInCurrentTab)
        } else {
            // If not all are selected, select all
            currentSelected.addAll(allItemsInCurrentTab)
        }

        _uiState.update {
            it.copy(deleteModeState = it.deleteModeState.copy(selectedItems = currentSelected))
        }
    }

    // 삭제 모드에서 삭제 버튼 클릭
    fun onDeleteSelectedItems() {
        _uiState.update {
            it.copy(deleteModeState = it.deleteModeState.copy(showDialog = true)) // DeleteModeState의 showDialog 사용
        }
    }
    private fun handleResult(result: Result<LikeResult>, tab: LikeTab) {
        result
            .onSuccess { (newLikes, nextCursor) ->
                val uiStateList = newLikes.map { it.toUiState() }
                when (tab) {
                    LikeTab.RECEIVED -> _uiState.update {
                        it.copy(
                            receivedLikes = uiStateList,
                            receivedLikesIsRefreshing = false,
                            receivedCursor = nextCursor
                        )
                    } //여기서
                    LikeTab.SENT -> _uiState.update {
                        it.copy(
                            sentLikes = uiStateList,
                            sentLikesIsRefreshing = false,
                            sentCursor = nextCursor
                        )
                    } //여기서
                    LikeTab.MATCHED -> _uiState.update {
                        it.copy(
                            matchedLikes = uiStateList,
                            matchedLikesIsRefreshing = false,
                            matchedCursor = nextCursor
                        )
                    } //여기서
                }
            }
            .onFailure { exception ->
                // 에러 처리 (예: 스낵바 표시, 로그 출력 등)
                println("Error loading likes for $tab: $exception")
            }

    }
    private fun handleLoadMoreResult(
        result: Result<LikeResult>,
        tab: LikeTab,
    ) {
        result
            .onSuccess { (newLikes, nextCursor) ->
                val uiStateList = newLikes.map { it.toUiState() }
                when (tab) {
                    LikeTab.RECEIVED -> _uiState.update {
                        it.copy(
                            receivedLikes = it.receivedLikes + uiStateList, // 기존 리스트에 추가
                            receivedLikesIsRefreshing = false,
                            receivedCursor = nextCursor // 커서 업데이트
                        )
                    }
                    LikeTab.SENT -> _uiState.update {
                        it.copy(
                            sentLikes = it.sentLikes + uiStateList, // 기존 리스트에 추가
                            sentLikesIsRefreshing = false,
                            sentCursor = nextCursor // 커서 업데이트
                        )
                    }
                    LikeTab.MATCHED -> _uiState.update {
                        it.copy(
                            matchedLikes = it.matchedLikes + uiStateList, // 기존 리스트에 추가
                            matchedLikesIsRefreshing = false,
                            matchedCursor = nextCursor // 커서 업데이트
                        )
                    }
                }
            }
            .onFailure {
                //에러 처리
            }
    }

    //LikeItem -> LikeItemUiState
    fun LikeItem.toUiState(): LikeItemUiState {
        return LikeItemUiState(
            id = this.id,
            thumbnail = this.thumbnail,
            nickname = this.nickname,
            age = this.age,
            introduction = this.introduction,
            receivedTime = this.receivedTime
        )
    }
}