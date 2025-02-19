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
    // private val getTrendingTagsUseCase: GetTrendingTagsUseCase, // UseCase는 주석처리.
    // private val searchTagsUseCase: SearchTagsUseCase // UseCase 주석처리
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyTagUiState())
    val uiState: StateFlow<MyTagUiState> = _uiState.asStateFlow()

    init {
        loadTags()
        loadRecentSearches() // 추가
    }

    fun onTagSearchQueryChanged(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
        searchTags(query) // 검색 기능 추가
    }

    // 태그 선택
    fun onTagSelected(tag: TagItemUiState) {
        if (!_uiState.value.selectedTags.contains(tag)) {
            _uiState.update {
                it.copy(selectedTags = it.selectedTags + tag)
            }
        }
    }

    // 태그 제거
    fun onTagRemoved(tag: TagItemUiState) {
        _uiState.update {
            it.copy(selectedTags = it.selectedTags - tag)
        }
    }

    // 검색 기능 (임시 - 테스트 데이터 사용)
    fun searchTags(query: String) {
        viewModelScope.launch {
            // UseCase 사용 (주석 처리)
            // val results = searchTagsUseCase(query)

            // 테스트 데이터 (실제로는 API나 DB에서 검색)
            val results = if (query.isNotBlank()) {
                listOf(
                    TagItemUiState("검색결과1_$query", "search_result_image1", 10),
                    TagItemUiState("검색결과2_$query", "search_result_image2", 20)
                )
            } else {
                listOf() // 빈 검색어면 빈 결과
            }
            _uiState.update {
                it.copy(searchResults = results)
            }
        }
    }

    //최근 검색어 임시 추가
    fun loadRecentSearches() {
        viewModelScope.launch {
            val recentSearches = listOf(
                TagItemUiState("최근검색1", "recent1", 1),
                TagItemUiState("최근검색2", "recent2", 2)
            )
            _uiState.update{
                it.copy(recentSearches = recentSearches)
            }
        }
    }
    fun onSearchPerformed() {
        // 검색 로직 (여기서는 간단하게 searchQuery를 사용)
        // searchTags(_uiState.value.searchQuery) //이미 다른곳에서 함
    }

    // 태그 로드 (테스트 데이터 사용, 이전과 동일)
    fun loadTags() {
        viewModelScope.launch {
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
