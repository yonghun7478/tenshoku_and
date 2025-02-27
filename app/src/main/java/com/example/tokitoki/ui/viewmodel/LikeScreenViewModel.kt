package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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


// TODO: 무한 스크롤 추가
@HiltViewModel
class LikeScreenViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(LikeScreenUiState())
    val uiState: StateFlow<LikeScreenUiState> = _uiState.asStateFlow()
    private val PAGE_SIZE = 20 // 한 번에 로드할 아이템 수

    init {
        loadLikes() // 초기 데이터 로드
    }

    // 초기 데이터 로드 함수
    private fun loadLikes() {
        _uiState.value = _uiState.value.copy(
            receivedLikes = createDummyLikes(LikeTab.RECEIVED, 0),
            sentLikes = createDummyLikes(LikeTab.SENT, 0),
            matchedLikes = createDummyLikes(LikeTab.MATCHED, 0)
        )
    }
    private fun createDummyLikes(tab: LikeTab, startIndex: Int): List<LikeItemUiState> {
        val baseId = when (tab) {
            LikeTab.RECEIVED -> 0
            LikeTab.SENT -> 100
            LikeTab.MATCHED -> 200
        }
        return List(PAGE_SIZE) {
            LikeItemUiState(
                id = baseId + startIndex + it, // startIndex를 사용하여 ID 계산
                thumbnail = "https://via.placeholder.com/150",
                nickname = "${tab.title} User ${startIndex + it}",
                age = 20 + it,
                introduction = "This is a sample introduction for ${tab.title} user ${startIndex + it}.",
                isChecked = false,
                isRefreshing = false // 초기 로딩 상태는 false
            )
        }
    }

    // 새로고침 함수 (현재 선택된 탭의 리스트만 갱신)
    fun refreshLikes() {
        viewModelScope.launch {
            val currentTab = _uiState.value.selectedTab
            when (currentTab) {
                LikeTab.RECEIVED -> {
                    _uiState.update {
                        it.copy(
                            receivedLikesIsRefreshing = true,
                            receivedLikes = emptyList()
                        )
                    }

                    delay(2000) // Simulate network delay
                    val newList = createDummyLikes(currentTab, 0)
                    _uiState.update {
                        it.copy(
                            receivedLikes = newList,
                            receivedLikesIsRefreshing = false
                        )
                    }
                }

                LikeTab.SENT -> {
                    _uiState.update {
                        it.copy(
                            sentLikesIsRefreshing = true,
                            sentLikes = emptyList()
                        )
                    }
                    delay(2000)
                    val newList = createDummyLikes(currentTab, 0)
                    _uiState.update {
                        it.copy(
                            sentLikes = newList,
                            sentLikesIsRefreshing = false
                        )
                    }

                }

                LikeTab.MATCHED -> {
                    _uiState.update {
                        it.copy(
                            matchedLikesIsRefreshing = true,
                            matchedLikes = emptyList()
                        )
                    }
                    delay(2000)
                    val newList = createDummyLikes(currentTab, 0)
                    _uiState.update {
                        it.copy(
                            matchedLikes = newList,
                            matchedLikesIsRefreshing = false
                        )
                    }
                }
            }
        }
    }

    // 추가 데이터 로드 함수
    fun loadMoreLikes() {
        viewModelScope.launch {
            val currentTab = _uiState.value.selectedTab
            val currentList = when (currentTab) {
                LikeTab.RECEIVED -> _uiState.value.receivedLikes
                LikeTab.SENT -> _uiState.value.sentLikes
                LikeTab.MATCHED -> _uiState.value.matchedLikes
            }
            val nextIndex = currentList.size // 현재 리스트 크기를 기준으로 다음 인덱스 계산

            val newLikes = createDummyLikes(currentTab, nextIndex) // 다음 인덱스부터 데이터 생성

            _uiState.update {
                when (currentTab) {
                    LikeTab.RECEIVED -> it.copy(receivedLikes = currentList + newLikes)
                    LikeTab.SENT -> it.copy(sentLikes = currentList + newLikes)
                    LikeTab.MATCHED -> it.copy(matchedLikes = currentList + newLikes)
                }
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

    fun onConfirmDeleteItem() { //itemId:Int를 인수로 받도록 변경
        // UseCase 호출 (현재는 구현되지 않았으므로 주석 처리)
        // deleteLikeItemUseCase(itemId)
        val itemId = _uiState.value.deleteItemState.itemId ?: return

        // 임시로 UI에서 직접 삭제 (실제 앱에서는 UseCase에서 처리)
        val currentReceived = _uiState.value.receivedLikes.toMutableList()
        val currentSent = _uiState.value.sentLikes.toMutableList()
        val currentMatched = _uiState.value.matchedLikes.toMutableList()

        val receivedIndexToRemove = currentReceived.indexOfFirst { it.id == itemId }
        if (receivedIndexToRemove != -1) {
            currentReceived.removeAt(receivedIndexToRemove)
        }

        val sentIndexToRemove = currentSent.indexOfFirst { it.id == itemId }
        if (sentIndexToRemove != -1) {
            currentSent.removeAt(sentIndexToRemove)
        }

        val matchedIndexToRemove = currentMatched.indexOfFirst { it.id == itemId }
        if (matchedIndexToRemove != -1) {
            currentMatched.removeAt(matchedIndexToRemove)
        }

        _uiState.value = _uiState.value.copy(
            receivedLikes = currentReceived,
            sentLikes = currentSent,
            matchedLikes = currentMatched,
            showSnackBar = true,
            deleteItemState = DeleteItemState()
        )

        // Show snackbar

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

    fun onConfirmDeleteSelectedItems() {
        val selectedItems = _uiState.value.deleteModeState.selectedItems

        val currentReceived = _uiState.value.receivedLikes.filterNot { it.id in selectedItems }
        val currentSent = _uiState.value.sentLikes.filterNot { it.id in selectedItems }
        val currentMatched = _uiState.value.matchedLikes.filterNot { it.id in selectedItems }

        _uiState.update{
            it.copy(
                receivedLikes = currentReceived,
                sentLikes = currentSent,
                matchedLikes = currentMatched,
                showSnackBar = true,
                deleteModeState = DeleteModeState() // DeleteModeState 초기화
            )
        }
    }

}