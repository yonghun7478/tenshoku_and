package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

// TODO: 리프레시할떄 아이템이 싹 날라간다음에 스크롤 위치를맨위로, 꾹 누르기 관련 버그 수정하기,무한 스크롤 추가
@HiltViewModel
class LikeScreenViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(LikeScreenUiState())
    val uiState: StateFlow<LikeScreenUiState> = _uiState.asStateFlow()

    init {
        loadLikes() // 초기 데이터 로드
    }

    // 초기 데이터 로드 함수
    private fun loadLikes() {
        _uiState.value = _uiState.value.copy(
            receivedLikes = createDummyLikes(LikeTab.RECEIVED),
            sentLikes = createDummyLikes(LikeTab.SENT),
            matchedLikes = createDummyLikes(LikeTab.MATCHED)
        )
    }


    // 각 탭별 더미 데이터 생성 함수
    private fun createDummyLikes(tab: LikeTab): List<LikeItemUiState> {
        val baseId = when (tab) {
            LikeTab.RECEIVED -> 0
            LikeTab.SENT -> 100
            LikeTab.MATCHED -> 200
        }
        return List(20) {
            LikeItemUiState(
                id = baseId + it,
                thumbnail = "https://via.placeholder.com/150",
                nickname = "${tab.title} User $it",
                age = 20 + it,
                introduction = "This is a sample introduction for ${tab.title} user $it.",
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
                    val newList = createDummyLikes(currentTab)
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
                    val newList = createDummyLikes(currentTab)
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
                    val newList = createDummyLikes(currentTab)
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

    // Event handling functions (called from UI)
    fun onTabSelected(tab: LikeTab) {
        _uiState.value =
            _uiState.value.copy(selectedTab = tab, selectedItems = emptySet()) // 탭 변경 시 선택된 항목 초기화
    }

    fun onToggleDeleteMode() {
        _uiState.value = _uiState.value.copy(
            isDeleteMode = !_uiState.value.isDeleteMode,
            selectedItems = emptySet() // 삭제 모드 전환 시 선택된 항목 초기화
        )
    }


    fun onItemLongClicked(itemId: Int) {
        //_uiState.value = _uiState.value.copy(showDeleteDialog = true)
        showDeleteConfirmationDialog(itemId)
    }

    //다이얼로그 표시
    fun showDeleteConfirmationDialog(itemId: Int) {
        _uiState.value = _uiState.value.copy(showDeleteDialog = true)
    }

    //다이얼로그 닫기
    fun onDismissDeleteDialog() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = false)
    }

    fun onConfirmDeleteItem(itemId: Int) { //itemId:Int를 인수로 받도록 변경
        // UseCase 호출 (현재는 구현되지 않았으므로 주석 처리)
        // deleteLikeItemUseCase(itemId)

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
            showDeleteDialog = false,
            showSnackBar = true
        )

        // Show snackbar

    }

    fun onDismissSnackBar() {
        _uiState.value = _uiState.value.copy(showSnackBar = false)
    }


    fun onSelectItem(itemId: Int) {
        val currentSelected = _uiState.value.selectedItems.toMutableSet()
        if (currentSelected.contains(itemId)) {
            currentSelected.remove(itemId)
        } else {
            currentSelected.add(itemId)
        }
        _uiState.value = _uiState.value.copy(selectedItems = currentSelected)
    }


    fun onSelectAllItems() {
        val currentTab = _uiState.value.selectedTab
        val allItemsInCurrentTab = when (currentTab) {
            LikeTab.RECEIVED -> _uiState.value.receivedLikes
            LikeTab.SENT -> _uiState.value.sentLikes
            LikeTab.MATCHED -> _uiState.value.matchedLikes
        }.map { it.id }

        val currentSelected = _uiState.value.selectedItems.toMutableSet()
        val areAllSelected = currentSelected.containsAll(allItemsInCurrentTab)

        if (areAllSelected) {
            // If all are selected, deselect all
            currentSelected.removeAll(allItemsInCurrentTab)
        } else {
            // If not all are selected, select all
            currentSelected.addAll(allItemsInCurrentTab)
        }

        _uiState.value = _uiState.value.copy(selectedItems = currentSelected)
    }


    fun onDeleteSelectedItems() {
        // Show confirmation dialog before deleting
        _uiState.value = _uiState.value.copy(showDeleteDialog = true)
    }

    fun onConfirmDeleteSelectedItems() {
        val selectedItems = _uiState.value.selectedItems
        val currentReceived = _uiState.value.receivedLikes.filterNot { it.id in selectedItems }
        val currentSent = _uiState.value.sentLikes.filterNot { it.id in selectedItems }
        val currentMatched = _uiState.value.matchedLikes.filterNot { it.id in selectedItems }

        _uiState.value = _uiState.value.copy(
            receivedLikes = currentReceived,
            sentLikes = currentSent,
            matchedLikes = currentMatched,
            selectedItems = emptySet(), // Clear selection after deletion
            isDeleteMode = false, // Exit delete mode
            showDeleteDialog = false,  // Close dialog
            showSnackBar = true
        )
    }

}