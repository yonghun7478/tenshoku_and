package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.common.ResultWrapper.ErrorType
import com.example.tokitoki.domain.usecase.GetTagDetailUseCase
import com.example.tokitoki.domain.usecase.IsTagSubscribedUseCase
import com.example.tokitoki.domain.usecase.GetTagSubscribersUseCase
import com.example.tokitoki.ui.state.TagDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagDetailViewModel @Inject constructor(
    private val getTagDetailUseCase: GetTagDetailUseCase,
    private val isTagSubscribedUseCase: IsTagSubscribedUseCase,
    private val getTagSubscribersUseCase: GetTagSubscribersUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(TagDetailUiState())
    val uiState: StateFlow<TagDetailUiState> = _uiState.asStateFlow()

    fun loadTagDetail(tagId: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            // 태그 상세, 구독여부, 구독자 리스트 병렬 로드
            try {
                val tagDeferred = async { getTagDetailUseCase(tagId) }
                val subscribedDeferred = async { isTagSubscribedUseCase(tagId) }
                val subscribersDeferred = async { getTagSubscribersUseCase(tagId, null, 20) }

                val tagResult = tagDeferred.await()
                val isSubscribed = subscribedDeferred.await()
                val subscribersResult = subscribersDeferred.await()

                tagResult.onSuccess { tag ->
                    _uiState.update { it.copy(tag = tag) }
                }.onFailure { e ->
                    _uiState.update { it.copy(error = e?.message, isLoading = false) }
                    return@launch
                }

                _uiState.update { it.copy(
                    isSubscribed = isSubscribed
                ) }

                subscribersResult.let { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            _uiState.update {
                                it.copy(
                                    subscribers = result.data.users,
                                    nextCursor = result.data.nextCursor,
                                    isLastPage = result.data.isLastPage,
                                    isLoading = false
                                )
                            }
                        }
                        is ResultWrapper.Error -> {
                            val errorMsg = when (val error = result.errorType) {
                                is ErrorType.ServerError -> "서버 오류(${error.httpCode}): ${error.message}"
                                is ErrorType.ExceptionError -> error.message
                            }
                            _uiState.update { it.copy(error = errorMsg, isLoading = false) }
                        }

                        ResultWrapper.Loading -> TODO()
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadMoreSubscribers(tagId: String) {
        val cursor = _uiState.value.nextCursor ?: return
        if (_uiState.value.isLastPage) return
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = getTagSubscribersUseCase(tagId, cursor, 20)
            when (result) {
                is ResultWrapper.Success -> {
                    _uiState.update {
                        it.copy(
                            subscribers = it.subscribers + result.data.users,
                            nextCursor = result.data.nextCursor,
                            isLastPage = result.data.isLastPage,
                            isLoading = false
                        )
                    }
                }
                is ResultWrapper.Error -> {
                    val errorMsg = when (val error = result.errorType) {
                        is ErrorType.ServerError -> "서버 오류(${error.httpCode}): ${error.message}"
                        is ErrorType.ExceptionError -> error.message
                    }
                    _uiState.update { it.copy(error = errorMsg, isLoading = false) }
                }

                ResultWrapper.Loading -> TODO()
            }
        }
    }
} 