package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.ui.state.MyTagUiState
import com.example.tokitoki.ui.state.TagItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTagViewModel @Inject constructor(
): ViewModel() {
    private val _uiState = MutableStateFlow(MyTagUiState())
    val uiState: StateFlow<MyTagUiState> = _uiState.asStateFlow()

    init {
        loadTags() // 초기 데이터 로드
    }

    fun onTagSearchQueryChanged(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
    }
    fun onTagSelected(tag: String) {
        if(!_uiState.value.selectedTags.contains(tag)){
            _uiState.update {
                it.copy(selectedTags = it.selectedTags + tag)
            }
        }
    }

    fun onTagRemoved(tag: String) {
        _uiState.update {
            it.copy(selectedTags = it.selectedTags - tag)
        }
    }

    fun onSearchPerformed() {
        //검색로직 임시 생략
    }

    // 태그 로드 함수 (테스트 데이터 사용)
    fun loadTags() {
        viewModelScope.launch { // 코루틴 사용
            // UseCase 호출 (주석 처리)
            // val trendingTags = getTrendingTagsUseCase()

            // 테스트 데이터 생성
            val todayTag = TagItemUiState("오늘의 태그", "today_image", 100)
            val trendingTags = listOf(
                TagItemUiState("트렌딩 태그1", "image1", 50),
                TagItemUiState("트렌딩 태그2", "image2", 120),
                TagItemUiState("트렌딩 태그3", "image3", 80)
            )
            val myTags = listOf(
                TagItemUiState("선택한 태그1", "my_image1", 30),
                TagItemUiState("선택한 태그2", "my_image2", 45),
            )
            val suggestedTags = listOf(
                TagItemUiState("추천 태그1", "suggested_image1", 15),
                TagItemUiState("추천 태그2", "suggested_image2", 33),
                TagItemUiState("추천 태그3", "suggested_image3", 77),
                TagItemUiState("추천 태그4", "suggested_image3", 77),
                TagItemUiState("추천 태그5", "suggested_image3", 77),
                TagItemUiState("추천 태그6", "suggested_image3", 77),
                TagItemUiState("추천 태그7", "suggested_image3", 77),
                TagItemUiState("추천 태그8", "suggested_image3", 77),
            )

            // UI 상태 업데이트
            _uiState.update {
                it.copy(
                    todayTags = todayTag,
                    trendingTags = trendingTags,
                    myTags = myTags,
                    suggestedTags = suggestedTags
                )
            }
        }
    }
}