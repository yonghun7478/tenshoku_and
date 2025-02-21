package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.usecase.AddRecentSearchUseCase
import com.example.tokitoki.domain.usecase.AddSelectedTagUseCase
import com.example.tokitoki.domain.usecase.DeleteRecentSearchUseCase
import com.example.tokitoki.domain.usecase.GetMyMainHomeTagsUseCase
import com.example.tokitoki.domain.usecase.GetRecentSearchesUseCase
import com.example.tokitoki.domain.usecase.GetSuggestedTagsUseCase
import com.example.tokitoki.domain.usecase.GetTodayTagUseCase
import com.example.tokitoki.domain.usecase.GetTrendingTagsUseCase
import com.example.tokitoki.domain.usecase.RemoveSelectedTagUseCase
import com.example.tokitoki.domain.usecase.RestoreTempSelectedTagsUseCase
import com.example.tokitoki.domain.usecase.SaveTempSelectedTagsUseCase
import com.example.tokitoki.domain.usecase.SearchTagsUseCase
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
    private val getTodayTagUseCase: GetTodayTagUseCase,
    private val getTrendingTagsUseCase: GetTrendingTagsUseCase,
    private val getMyTagsUseCase: GetMyMainHomeTagsUseCase,
    private val getSuggestedTagsUseCase: GetSuggestedTagsUseCase,
    private val searchTagsUseCase: SearchTagsUseCase,
    private val getRecentSearchesUseCase: GetRecentSearchesUseCase,
    private val addSelectedTagUseCase: AddSelectedTagUseCase,
    private val removeSelectedTagUseCase: RemoveSelectedTagUseCase,
    private val saveTempSelectedTagsUseCase: SaveTempSelectedTagsUseCase,
    private val restoreTempSelectedTagsUseCase: RestoreTempSelectedTagsUseCase,
//    private val addRecentSearchUseCase: AddRecentSearchUseCase, // 추가
//    private val deleteRecentSearchUseCase: DeleteRecentSearchUseCase // 추가
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainHomeMyTagUiState())
    val uiState: StateFlow<MainHomeMyTagUiState> = _uiState.asStateFlow()

    init {
        loadTags()
        loadRecentSearches()
    }

    fun onTagSearchQueryChanged(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
        searchTags(query)
    }

    // 검색어 초기화 함수
    fun clearSearchQuery() {
        _uiState.update {
            it.copy(searchQuery = "")
        }
    }

    // 확장 모드 진입 시 현재 선택된 태그를 임시 저장
    fun saveSelectedTags() {
        viewModelScope.launch {
            saveTempSelectedTagsUseCase(_uiState.value.selectedTags.map { it.toDomain() })
        }
    }

    // 돌아가기 시 임시 저장된 태그로 복원
    fun restoreSelectedTags() {
        viewModelScope.launch {
            val result = restoreTempSelectedTagsUseCase()
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(selectedTags = result.getOrThrow().map { tag -> tag.toPresentation() })
                }
            } else {
                // 에러 처리
            }
        }
    }

    // 태그 선택
    fun onTagSelected(tag: MainHomeTagItemUiState) {
        viewModelScope.launch {
            val tagItem = tag.toDomain()
            val result = addSelectedTagUseCase(tagItem)
            if(result.isSuccess){
                if (!_uiState.value.selectedTags.contains(tag)) {
                    _uiState.update {
                        it.copy(selectedTags = it.selectedTags + tag)
                    }
                }
            }

        }

    }

    // 태그 제거
    fun onTagRemoved(tag: MainHomeTagItemUiState) {
        viewModelScope.launch {
            val tagItem = tag.toDomain()
            val result = removeSelectedTagUseCase(tagItem)
            if(result.isSuccess){
                _uiState.update {
                    it.copy(selectedTags = it.selectedTags - tag)
                }
            }
        }
    }

    //검색 usecase
    fun searchTags(query: String) {
        viewModelScope.launch {
            val result = searchTagsUseCase(query)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(searchResults = result.getOrThrow().map { tag -> tag.toPresentation() })
                }
            } else {
                // 에러 처리
            }
        }
    }

    fun loadRecentSearches() {
        viewModelScope.launch {
            val result = getRecentSearchesUseCase()
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(recentSearches = result.getOrThrow().map { tag -> tag.toPresentation()})
                }
            } else {
                // 에러 처리
            }
        }
    }


    //검색 usecase
    //최근 검색 usecase
    fun onSearchPerformed() {
        // 검색 버튼 눌렀을 때 추가 작업 (예: 최근 검색어 추가)
//        viewModelScope.launch {
//            val currentQuery = _uiState.value.searchQuery
//            if (currentQuery.isNotBlank()) {
//                val result = addRecentSearchUseCase(MainHomeTag(currentQuery, "", 0)) // 최근 검색어 객체 생성, MainHomeTag 타입으로
//                if (result.isSuccess.not()) {
//                    // 에러 처리 (최근 검색어 추가 실패)
//                }
//            }
//        }
    }

    fun loadTags() {
        viewModelScope.launch {
            val todayTagResult = getTodayTagUseCase()
            val trendingTagsResult = getTrendingTagsUseCase()
            val myTagsResult = getMyTagsUseCase()
            val suggestedTagsResult = getSuggestedTagsUseCase()

            if (todayTagResult.isSuccess && trendingTagsResult.isSuccess &&
                myTagsResult.isSuccess && suggestedTagsResult.isSuccess
            ) {
                _uiState.update {
                    it.copy(
                        todayTags = todayTagResult.getOrThrow().toPresentation(),
                        trendingTags = trendingTagsResult.getOrThrow().map { it -> it.toPresentation() },
                        myTags = myTagsResult.getOrThrow().map { it -> it.toPresentation() },
                        suggestedTags = suggestedTagsResult.getOrThrow().map { it-> it.toPresentation() }
                    )
                }
            } else {
                // 에러 처리 (하나라도 실패하면)
                // 예: todayTagResult.exceptionOrNull() 등을 사용하여 에러 원인 확인
            }
        }
    }
}