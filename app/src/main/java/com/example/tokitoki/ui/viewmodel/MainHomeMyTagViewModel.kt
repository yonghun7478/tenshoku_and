package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.model.MyTag
import com.example.tokitoki.domain.model.TagType
import com.example.tokitoki.domain.usecase.GetMyMainHomeTagsUseCase
import com.example.tokitoki.domain.usecase.GetSuggestedTagsUseCase
import com.example.tokitoki.domain.usecase.GetTodayTagUseCase
import com.example.tokitoki.domain.usecase.GetTrendingTagsUseCase
import com.example.tokitoki.domain.usecase.SetMyTagUseCase
import com.example.tokitoki.domain.usecase.tag.RemoveUserTagUseCase
import com.example.tokitoki.ui.state.MainHomeMyTagUiState
import com.example.tokitoki.ui.state.SuggestedTagsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

private const val TAG = "MainHomeMyTagViewModel"

@HiltViewModel
class MainHomeMyTagViewModel @Inject constructor(
    private val getTodayTagUseCase: GetTodayTagUseCase,
    private val getTrendingTagsUseCase: GetTrendingTagsUseCase,
    private val getMyTagsUseCase: GetMyMainHomeTagsUseCase,
    private val getSuggestedTagsUseCase: GetSuggestedTagsUseCase,
    private val setMyTagUseCase: SetMyTagUseCase,
    private val removeUserTagUseCase: RemoveUserTagUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainHomeMyTagUiState())
    val uiState: StateFlow<MainHomeMyTagUiState> = _uiState.asStateFlow()

    private val _suggestedTagsUiState = MutableStateFlow(SuggestedTagsUiState())
    val suggestedTagsUiState: StateFlow<SuggestedTagsUiState> = _suggestedTagsUiState.asStateFlow()

    init {
        loadInitialTags()
        Log.d(TAG, "ViewModel initialized, loadInitialTags() called.")
    }

    fun loadInitialTags() {
        viewModelScope.launch {
            Log.d(TAG, "loadInitialTags() called. Setting loading states to true.")
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
                Log.d(TAG, "loadInitialTags() completed successfully. Loading states set to false.")
            } else {
                // 에러 처리 (하나라도 실패하면)
                _uiState.update {
                    it.copy(
                        isLoadingTodayAndTrending = false,
                        isLoadingMyTags = false,
                        isLoadingSuggestedTags = false
                    )
                }
                Log.e(TAG, "loadInitialTags() failed.")
            }
        }
    }

    fun refreshMyAndSuggestedTags() {
        viewModelScope.launch {
            Log.d(TAG, "refreshMyAndSuggestedTags() called. Loading myTags and suggestedTags.")
            val myTagsResult = getMyTagsUseCase()
            val suggestedTagsResult = getSuggestedTagsUseCase()

            if (myTagsResult.isSuccess && suggestedTagsResult.isSuccess) {
                _uiState.update {
                    it.copy(
                        myTags = myTagsResult.getOrThrow().map { it.toPresentation() },
                    )
                }
                _suggestedTagsUiState.update {
                    it.copy(
                        tags = suggestedTagsResult.getOrThrow().map { it.toPresentation() },
                        canLoadMore = true,
                    )
                }
                Log.d(TAG, "refreshMyAndSuggestedTags() completed successfully.")
            } else {
                showSnackbarMessage("タグの更新中にエラーが発生しました。")
                Log.e(TAG, "refreshMyAndSuggestedTags() failed.")
            }
        }
    }

    fun loadMoreSuggestedTags() {
        viewModelScope.launch {
            Log.d(TAG, "loadMoreSuggestedTags() called.")
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
                    Log.d(TAG, "loadMoreSuggestedTags() completed successfully. Added ${newTags.size} new tags.")
                } else {
                    Log.d(TAG, "loadMoreSuggestedTags() completed, no new tags.")
                }
            } else {
                // 에러 처리
                Log.e(TAG, "loadMoreSuggestedTags() failed.")
            }
        }
    }

    // 스낵바 메시지 표시
    fun showSnackbarMessage(message: String) {
        _uiState.update {
            it.copy(snackbarMessage = message)
        }
        Log.d(TAG, "Snackbar message set: $message")
    }

    // 스낵바 메시지 초기화
    fun clearSnackbarMessage() {
        _uiState.update {
            it.copy(snackbarMessage = null)
        }
        Log.d(TAG, "Snackbar message cleared.")
    }

    // 태그 선택 토글 (실제 구독 로직을 포함하도록 변경)
    fun onTagToggleSubscription(tagId: String, isCurrentlySubscribed: Boolean, tagType: TagType) {
        viewModelScope.launch {
            Log.d(TAG, "onTagToggleSubscription() called. tagId: $tagId, isCurrentlySubscribed: $isCurrentlySubscribed")
            val result = if (isCurrentlySubscribed) {
                // 현재 구독 중이면 구독 취소 (태그 제거)
                removeUserTagUseCase(tagId.toInt())
                Result.success(Unit)
            } else {
                // 현재 구독 중이 아니면 구독 (태그 추가)
                setMyTagUseCase(listOf(MyTag(tagId = tagId.toInt(), tagTypeId = tagType.ordinal)))
            }

            result.onSuccess {
                // 구독/구독 취소 성공 시 UI 상태 업데이트
                showSnackbarMessage(if (isCurrentlySubscribed) "タグが解除されました" else "タグが登録されました")
                refreshMyAndSuggestedTags() // UI에서 직접 새로고침하므로, 여기서도 호출해 즉각적인 피드백을 줌
            }.onFailure {
                // 오류 처리 (예: 스낵바 메시지 표시)
                showSnackbarMessage("エラーが発生しました: ${it.message ?: "不明なエラー"}")
                Log.e(TAG, "onTagToggleSubscription() failed: ${it.message}")
            }
        }
    }
}