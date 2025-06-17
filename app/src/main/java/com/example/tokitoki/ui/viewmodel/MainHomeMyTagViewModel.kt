package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.GetMyMainHomeTagsUseCase
import com.example.tokitoki.domain.usecase.GetSuggestedTagsUseCase
import com.example.tokitoki.domain.usecase.GetTodayTagUseCase
import com.example.tokitoki.domain.usecase.GetTrendingTagsUseCase
import com.example.tokitoki.domain.usecase.SubscribeTagUseCase
import com.example.tokitoki.domain.usecase.UnsubscribeTagUseCase
import com.example.tokitoki.ui.state.MainHomeMyTagUiState
import com.example.tokitoki.ui.state.SuggestedTagsUiState
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
    private val subscribeTagUseCase: SubscribeTagUseCase,
    private val unsubscribeTagUseCase: UnsubscribeTagUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainHomeMyTagUiState())
    val uiState: StateFlow<MainHomeMyTagUiState> = _uiState.asStateFlow()

    private val _suggestedTagsUiState = MutableStateFlow(SuggestedTagsUiState())
    val suggestedTagsUiState: StateFlow<SuggestedTagsUiState> = _suggestedTagsUiState.asStateFlow()


    init {
        loadTags()
    }

    fun loadTags() {
        viewModelScope.launch {
            // 모든 로딩 상태를 true로 설정
            _uiState.update {
                it.copy(
                    isLoadingTodayAndTrending = true,
                    isLoadingMyTags = true,
                    isLoadingSuggestedTags = true
                )
            }

            val todayTagResult = getTodayTagUseCase()
            val trendingTagsResult = getTrendingTagsUseCase()
            val myTagsResult = getMyTagsUseCase()
            val suggestedTagsResult = getSuggestedTagsUseCase()

            if (todayTagResult.isSuccess && trendingTagsResult.isSuccess &&
                myTagsResult.isSuccess && suggestedTagsResult.isSuccess
            ) {
                _uiState.update {
                    it.copy(
                        todayTag = todayTagResult.getOrThrow().toPresentation(),
                        trendingTags = trendingTagsResult.getOrThrow()
                            .map { it -> it.toPresentation() },
                        myTags = myTagsResult.getOrThrow().map { it -> it.toPresentation() },
                        isLoadingTodayAndTrending = false,
                        isLoadingMyTags = false,
                        isLoadingSuggestedTags = false
                    )
                }

                _suggestedTagsUiState.update {
                    it.copy(
                        tags = suggestedTagsResult.getOrThrow().map { it.toPresentation() },
                        canLoadMore = true,
                    )
                }
            } else {
                // 에러 처리 (하나라도 실패하면)
                _uiState.update {
                    it.copy(
                        isLoadingTodayAndTrending = false,
                        isLoadingMyTags = false,
                        isLoadingSuggestedTags = false
                    )
                }
            }
        }
    }

    fun loadMoreSuggestedTags() {
        viewModelScope.launch {
            val result = getSuggestedTagsUseCase()

            if (result.isSuccess) {
                val newTags = result.getOrThrow().map { it.toPresentation() }
                if (newTags.isNotEmpty()) {
                    _suggestedTagsUiState.update { currentState -> // update 사용
                        currentState.copy(
                            tags = currentState.tags + newTags, // 기존 태그에 새 태그 추가
                            canLoadMore = true
                        )
                    }
                }
            } else {
                // 에러 처리
            }
        }
    }

    // 스낵바 메시지 표시
    fun showSnackbarMessage(message: String) {
        _uiState.update {
            it.copy(snackbarMessage = message)
        }
    }

    // 스낵바 메시지 초기화
    fun clearSnackbarMessage() {
        _uiState.update {
            it.copy(snackbarMessage = null)
        }
    }

    // 태그 선택 토글 (실제 구독 로직을 포함하도록 변경)
    fun onTagToggleSubscription(tagId: String, isCurrentlySubscribed: Boolean) {
        viewModelScope.launch {
            val result = if (isCurrentlySubscribed) {
                unsubscribeTagUseCase(tagId)
            } else {
                subscribeTagUseCase(tagId)
            }

            result.onSuccess {
                // 구독/구독 취소 성공 시 UI 상태 업데이트
                loadTags() // 태그 목록을 다시 로드하여 최신 구독 상태 반영
                showSnackbarMessage(if (isCurrentlySubscribed) "タグが解除されました" else "タグが登録されました")
            }.onFailure { e ->
                // 오류 처리 (예: 스낵바 메시지 표시)
                showSnackbarMessage("エラーが発生しました: ${e.message ?: "不明なエラー"}")
            }
        }
    }
}