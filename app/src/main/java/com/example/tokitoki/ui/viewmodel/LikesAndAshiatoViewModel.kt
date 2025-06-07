package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.model.LikeItem
import com.example.tokitoki.domain.model.LikeResult
import com.example.tokitoki.domain.usecase.AddUserIdsToCacheUseCase
import com.example.tokitoki.domain.usecase.GetAshiatoPageUseCase
import com.example.tokitoki.domain.usecase.GetLikesUseCase
import com.example.tokitoki.ui.state.AshiatoUiState
import com.example.tokitoki.ui.state.LikeItemUiState
import com.example.tokitoki.ui.state.LikeScreenUiState
import com.example.tokitoki.ui.state.LikesAndAshiatoTab
import com.example.tokitoki.ui.state.LikesAndAshiatoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.tokitoki.domain.repository.LikeRepository

@HiltViewModel
class LikesAndAshiatoViewModel @Inject constructor(
    private val getLikesUseCase: GetLikesUseCase,
    private val getAshiatoPageUseCase: GetAshiatoPageUseCase,
    private val addUserIdsToCacheUseCase: AddUserIdsToCacheUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LikesAndAshiatoUiState())
    val uiState = _uiState.asStateFlow()

    private var ashiatoNextCursor: String? = null
    private var isAshiatoLoading = false

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        loadLikes(isInitialLoad = true)
        loadAshiato(isInitialLoad = true)
    }

    fun changeTab(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }

    fun refresh() {
        when (_uiState.value.selectedTabIndex) {
            LikesAndAshiatoTab.LIKES.ordinal -> refreshLikes()
            LikesAndAshiatoTab.ASHIATO.ordinal -> refreshAshiato()
        }
    }

    fun loadMore() {
        when (_uiState.value.selectedTabIndex) {
            LikesAndAshiatoTab.LIKES.ordinal -> loadMoreLikes()
            LikesAndAshiatoTab.ASHIATO.ordinal -> loadMoreAshiato()
        }
    }

    // Like-related methods
    private fun refreshLikes() {
        _uiState.update {
            it.copy(likeState = it.likeState.copy(receivedLikesIsRefreshing = true, receivedLikes = emptyList()))
        }
        loadLikes(isInitialLoad = false)
    }

    private fun loadMoreLikes() {
        val cursor = _uiState.value.likeState.receivedCursor
        loadLikes(isInitialLoad = false, cursor = cursor)
    }

    private fun loadLikes(isInitialLoad: Boolean, cursor: Long? = null) {
        viewModelScope.launch {
            getLikesUseCase(LikeRepository.RECEIVED, cursor)
                .onSuccess { (newLikes, nextCursor) ->
                    val newLikeUiStates = newLikes.map { it.toUiState() }
                    _uiState.update { currentState ->
                        val currentLikes = if (isInitialLoad || currentState.likeState.receivedLikesIsRefreshing) {
                            newLikeUiStates
                        } else {
                            currentState.likeState.receivedLikes + newLikeUiStates
                        }
                        currentState.copy(
                            likeState = currentState.likeState.copy(
                                receivedLikes = currentLikes,
                                receivedCursor = nextCursor,
                                receivedLikesIsRefreshing = false
                            )
                        )
                    }
                }.onFailure {
                    _uiState.update { currentState ->
                        currentState.copy(
                            likeState = currentState.likeState.copy(
                                receivedLikesIsRefreshing = false
                            )
                        )
                    }
                }
        }
    }

    // Ashiato-related methods
    private fun refreshAshiato() {
        ashiatoNextCursor = null
        loadAshiato(isRefresh = true)
    }

    private fun loadMoreAshiato() {
        if (isAshiatoLoading || ashiatoNextCursor == null) return
        loadAshiato(cursorToFetch = ashiatoNextCursor)
    }

    private fun loadAshiato(
        cursorToFetch: String? = null,
        isInitialLoad: Boolean = false,
        isRefresh: Boolean = false
    ) {
        if (isAshiatoLoading) return
        isAshiatoLoading = true

        _uiState.update {
            it.copy(
                ashiatoState = it.ashiatoState.copy(
                    isLoadingInitial = isInitialLoad,
                    isRefreshing = isRefresh,
                    error = null
                )
            )
        }

        viewModelScope.launch {
            getAshiatoPageUseCase(cursor = cursorToFetch)
                .onSuccess { resultPage ->
                    _uiState.update { currentState ->
                        val newLogs = if (isInitialLoad || isRefresh) {
                            resultPage.logs
                        } else {
                            currentState.ashiatoState.timeline.dailyLogs + resultPage.logs
                        }
                        ashiatoNextCursor = resultPage.nextCursor
                        val newTimeline = currentState.ashiatoState.timeline.copy(dailyLogs = newLogs)

                        currentState.copy(
                            ashiatoState = currentState.ashiatoState.copy(
                                isLoadingInitial = false,
                                isRefreshing = false,
                                timeline = newTimeline,
                                canLoadMore = resultPage.nextCursor != null,
                                error = null
                            )
                        )
                    }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            ashiatoState = it.ashiatoState.copy(
                                isLoadingInitial = false,
                                isRefreshing = false,
                                error = error
                            )
                        )
                    }
                }
            isAshiatoLoading = false
        }
    }
    
    fun addUserIdsToCache(date: String) {
        viewModelScope.launch {
            val dailyLog = _uiState.value.ashiatoState.timeline.dailyLogs.find { it.date == date }
            dailyLog?.let { log ->
                val userIds = log.viewers.map { it.id }
                addUserIdsToCacheUseCase(
                    userIds = userIds,
                    screenName = "AshiatoScreen" // Or a new screen name
                )
            }
        }
    }

    private fun LikeItem.toUiState(): LikeItemUiState {
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