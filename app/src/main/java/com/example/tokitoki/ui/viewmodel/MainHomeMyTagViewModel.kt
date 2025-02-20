package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.ui.state.MainHomeMyTagUiState
import com.example.tokitoki.ui.state.MainHomeTagItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainHomeMyTagViewModel @Inject constructor(
    // private val getTrendingTagsUseCase: GetTrendingTagsUseCase, // UseCase는 주석처리.
    // private val searchTagsUseCase: SearchTagsUseCase // UseCase 주석처리
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainHomeMyTagUiState())
    val uiState: StateFlow<MainHomeMyTagUiState> = _uiState.asStateFlow()

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

    // 검색어 초기화 함수
    fun clearSearchQuery() {
        _uiState.update {
            it.copy(searchQuery = "") // searchQuery를 빈 문자열로 설정
        }
    }

    // 태그 선택
    fun onTagSelected(tag: MainHomeTagItemUiState) {
        if (!_uiState.value.selectedTags.contains(tag)) {
            _uiState.update {
                it.copy(selectedTags = it.selectedTags + tag)
            }
        }
    }

    // 태그 제거
    fun onTagRemoved(tag: MainHomeTagItemUiState) {
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
                    MainHomeTagItemUiState("검색결과1_$query", "search_result_image1", 10),
                    MainHomeTagItemUiState("검색결과2_$query", "search_result_image2", 20)
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
                MainHomeTagItemUiState("최근검색1", "recent1", 1),
                MainHomeTagItemUiState("최근검색2", "recent2", 2)
            )
            _uiState.update {
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
            val todayTag = MainHomeTagItemUiState("오늘의 태그", "today_image", 100)
            val trendingTags = listOf(
                MainHomeTagItemUiState("트렌딩 태그1", "image1", 50),
                MainHomeTagItemUiState("트렌딩 태그2", "image2", 120),
                MainHomeTagItemUiState("트렌딩 태그3", "image3", 80)
            )
            val myTags = listOf(
                MainHomeTagItemUiState("선택한 태그1", "my_image1", 30),
                MainHomeTagItemUiState("선택한 태그2", "my_image2", 45),
            )
            val suggestedTags = listOf(
                MainHomeTagItemUiState("추천 태그1", "suggested_image1", 15),
                MainHomeTagItemUiState("추천 태그2", "suggested_image2", 33),
                MainHomeTagItemUiState("추천 태그3", "suggested_image3", 77),
                MainHomeTagItemUiState("추천 태그4", "suggested_image3", 77),
                MainHomeTagItemUiState("추천 태그5", "suggested_image3", 77),
                MainHomeTagItemUiState("추천 태그6", "suggested_image3", 77),
                MainHomeTagItemUiState("추천 태그7", "suggested_image3", 77),
                MainHomeTagItemUiState("추천 태그8", "suggested_image3", 77),
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
