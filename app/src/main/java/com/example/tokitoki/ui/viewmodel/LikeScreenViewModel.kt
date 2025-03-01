package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.model.LikeItem
import com.example.tokitoki.domain.usecase.ClearLikeItemsUseCase
import com.example.tokitoki.domain.usecase.DeleteLikeItemUseCase
import com.example.tokitoki.domain.usecase.DeleteSelectedLikeItemsUseCase
import com.example.tokitoki.domain.usecase.GetMatchedLikesUseCase
import com.example.tokitoki.domain.usecase.GetReceivedLikesUseCase
import com.example.tokitoki.domain.usecase.GetSentLikesUseCase
import com.example.tokitoki.domain.usecase.LoadMoreLikesUseCase
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
    private val getReceivedLikesUseCase: GetReceivedLikesUseCase,
    private val getSentLikesUseCase: GetSentLikesUseCase,
    private val getMatchedLikesUseCase: GetMatchedLikesUseCase,
    private val deleteLikeItemUseCase: DeleteLikeItemUseCase,
    private val deleteSelectedLikeItemsUseCase: DeleteSelectedLikeItemsUseCase,
    private val loadMoreLikesUseCase: LoadMoreLikesUseCase,
    private val clearLikeItemsUseCase: ClearLikeItemsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LikeScreenUiState())
    val uiState: StateFlow<LikeScreenUiState> = _uiState.asStateFlow()

    init {
        loadLikes()
    }

    private fun loadLikes() {
        viewModelScope.launch {
            // UseCases를 사용하여 데이터 로드
            handleResult(getReceivedLikesUseCase(), LikeTab.RECEIVED)
            handleResult(getSentLikesUseCase(), LikeTab.SENT)
            handleResult(getMatchedLikesUseCase(), LikeTab.MATCHED)
        }
    }

    fun refreshLikes() {
        viewModelScope.launch {
            val currentTab = _uiState.value.selectedTab
            val currentList = when (currentTab) {
                LikeTab.RECEIVED -> _uiState.value.receivedLikes
                LikeTab.SENT -> _uiState.value.sentLikes
                LikeTab.MATCHED -> _uiState.value.matchedLikes
            }
            when (currentTab) {
                LikeTab.RECEIVED -> {
                    _uiState.update {
                        it.copy(
                            receivedLikesIsRefreshing = true,
                            receivedLikes = emptyList()
                        )
                    }

                    delay(500)

                    clearLikeItemsUseCase(currentTab.title)
                    handleLoadMoreResult(
                        loadMoreLikesUseCase(currentTab.title, 0),
                        currentTab,
                        currentList
                    )
                }

                LikeTab.SENT -> { // LikeTab.SENT, LikeTab.MATCHED 도 동일하게 수정
                    _uiState.update {
                        it.copy(
                            sentLikesIsRefreshing = true,
                            sentLikes = emptyList()
                        )
                    }

                    delay(500)

                    clearLikeItemsUseCase(currentTab.title)
                    handleLoadMoreResult(
                        loadMoreLikesUseCase(currentTab.title, 0),
                        currentTab,
                        currentList
                    )
                }

                LikeTab.MATCHED -> {
                    _uiState.update {
                        it.copy(
                            matchedLikesIsRefreshing = true,
                            matchedLikes = emptyList()
                        )
                    }

                    delay(500)

                    clearLikeItemsUseCase(currentTab.title)
                    handleLoadMoreResult(
                        loadMoreLikesUseCase(currentTab.title, 0),
                        currentTab,
                        currentList
                    )
                }
            }
        }
    }

    fun loadMoreLikes() {
        viewModelScope.launch {
            val currentTab = _uiState.value.selectedTab
            val currentList = when (currentTab) {
                LikeTab.RECEIVED -> _uiState.value.receivedLikes
                LikeTab.SENT -> _uiState.value.sentLikes
                LikeTab.MATCHED -> _uiState.value.matchedLikes
            }
            val nextIndex = currentList.size

            handleLoadMoreResult(
                loadMoreLikesUseCase(currentTab.title, nextIndex),
                currentTab,
                currentList
            )

        }
    }

    // 단일 아이템 삭제
    fun onConfirmDeleteItem() {
        val itemId = _uiState.value.deleteItemState.itemId ?: return
        viewModelScope.launch {

            deleteLikeItemUseCase(itemId)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            showSnackBar = true,
                            deleteItemState = DeleteItemState()
                        )
                    }
                    loadLikes()
                }
                .onFailure {
                    //에러 핸들링
                }
        }
    }

    // 선택된 아이템들 삭제
    fun onConfirmDeleteSelectedItems() {

        viewModelScope.launch {
            val selectedItems = _uiState.value.deleteModeState.selectedItems
            deleteSelectedLikeItemsUseCase(selectedItems)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            deleteModeState = DeleteModeState(),
                            showSnackBar = true
                        )
                    }
                    loadLikes()
                }
                .onFailure {
                    //에러 핸들링
                }
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


    fun onItemLongClicked(itemId: Int) {
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


    fun onSelectItem(itemId: Int) {
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

    private fun handleResult(result: Result<List<LikeItem>>, tab: LikeTab) {
        result
            .onSuccess { likes ->
                val uiStateList = likes.map { it.toUiState() }
                when (tab) {
                    LikeTab.RECEIVED -> _uiState.update {
                        it.copy(
                            receivedLikes = uiStateList,
                            receivedLikesIsRefreshing = false
                        )
                    } //여기서
                    LikeTab.SENT -> _uiState.update {
                        it.copy(
                            sentLikes = uiStateList,
                            sentLikesIsRefreshing = false
                        )
                    } //여기서
                    LikeTab.MATCHED -> _uiState.update {
                        it.copy(
                            matchedLikes = uiStateList,
                            matchedLikesIsRefreshing = false
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
        result: Result<List<LikeItem>>,
        tab: LikeTab,
        currentList: List<LikeItemUiState>
    ) {
        result
            .onSuccess { newLikes ->
                val uiStateList = newLikes.map { it.toUiState() }
                when (tab) {
                    LikeTab.RECEIVED -> _uiState.update {
                        it.copy(
                            receivedLikes = currentList + uiStateList,
                            receivedLikesIsRefreshing = false
                        )
                    }

                    LikeTab.SENT -> _uiState.update {
                        it.copy(
                            sentLikes = currentList + uiStateList,
                            sentLikesIsRefreshing = false
                        )
                    }

                    LikeTab.MATCHED -> _uiState.update {
                        it.copy(
                            matchedLikes = currentList + uiStateList,
                            matchedLikesIsRefreshing = false
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
            introduction = this.introduction
        )
    }
}