package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.model.LikeItem
import com.example.tokitoki.domain.model.LikeResult
import com.example.tokitoki.domain.usecase.GetLikesUseCase
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
            handleResult(getLikesUseCase(LikeTab.RECEIVED.title, null))
        }
    }

    fun refreshLikes() {
        viewModelScope.launch {
            _uiState.update { it.copy(receivedLikesIsRefreshing = true, receivedLikes = emptyList()) }
            delay(500)
            handleResult(getLikesUseCase(LikeTab.RECEIVED.title, null)) //데이터를 다시 가져온다.
        }
    }

    fun loadMoreLikes() {
        viewModelScope.launch {
            val cursor = _uiState.value.receivedCursor
            handleLoadMoreResult(
                getLikesUseCase(LikeTab.RECEIVED.title, cursor)
            )
        }
    }

    private fun handleResult(result: Result<LikeResult>) {
        result
            .onSuccess { (newLikes, nextCursor) ->
                val uiStateList = newLikes.map { it.toUiState() }
                _uiState.update {
                    it.copy(
                        receivedLikes = uiStateList,
                        receivedLikesIsRefreshing = false,
                        receivedCursor = nextCursor
                    )
                }
            }
            .onFailure { exception ->
                // 에러 처리 (예: 스낵바 표시, 로그 출력 등)
                println("Error loading likes: $exception")
            }
    }

    private fun handleLoadMoreResult(
        result: Result<LikeResult>,
    ) {
        result
            .onSuccess { (newLikes, nextCursor) ->
                val uiStateList = newLikes.map { it.toUiState() }
                _uiState.update {
                    it.copy(
                        receivedLikes = it.receivedLikes + uiStateList, // 기존 리스트에 추가
                        receivedLikesIsRefreshing = false,
                        receivedCursor = nextCursor // 커서 업데이트
                    )
                }
            }
            .onFailure { exception ->
                // 에러 처리 (예: 스낵바 표시, 로그 출력 등)
                println("Error loading more likes: $exception")
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