package com.example.tokitoki.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.model.Message
import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.usecase.GetUserDetailUseCase
import com.example.tokitoki.domain.usecase.message.GetMessageHistoryUseCase
import com.example.tokitoki.domain.usecase.MoveMessageToPreviousUseCase
import com.example.tokitoki.domain.usecase.message.ReceiveMessageUseCase
import com.example.tokitoki.domain.usecase.message.SendMessageUseCase
import com.example.tokitoki.domain.usecase.message.UpdateMessageStatusUseCase
import com.example.tokitoki.domain.usecase.tag.GetUserTagsUseCase
import com.example.tokitoki.ui.state.MessageDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class MessageDetailViewModel @Inject constructor(
    private val getUserDetailUseCase: GetUserDetailUseCase,
    private val getMessageHistoryUseCase: GetMessageHistoryUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val receiveMessageUseCase: ReceiveMessageUseCase,
    private val updateMessageStatusUseCase: UpdateMessageStatusUseCase,
    private val moveMessageToPreviousUseCase: MoveMessageToPreviousUseCase,
    private val getUserTagsUseCase: GetUserTagsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessageDetailUiState())
    val uiState: StateFlow<MessageDetailUiState> = _uiState.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private var currentCursor: String? = null

    private val TAG = "MessageDetailViewModel"

    private val _shouldScrollToBottom = MutableStateFlow(false)
    val shouldScrollToBottom: StateFlow<Boolean> = _shouldScrollToBottom.asStateFlow()

    fun initialize(otherUserId: String) {
        viewModelScope.launch {
            try {
                // 사용자 프로필 로드
                val profileResult = getUserDetailUseCase(otherUserId)
                when (profileResult) {
                    is ResultWrapper.Success -> {
                        _uiState.update { it.copy(userProfile = profileResult.data) }
                    }

                    is ResultWrapper.Error -> {
                        _uiState.update {
                            it.copy(
                                error = when (profileResult.errorType) {
                                    is ResultWrapper.ErrorType.ServerError -> {
                                        it.error.toString()
                                    }

                                    is ResultWrapper.ErrorType.ExceptionError -> {
                                        it.error.toString()
                                    }

                                    else -> {
                                        "error"
                                    }
                                }
                            )
                        }
                    }

                    ResultWrapper.Loading -> TODO()
                }

                // 사용자 태그 로드
                getUserTagsUseCase(otherUserId)
                    .onSuccess { tags ->
                        _uiState.update { it.copy(userTags = tags) }
                    }
                    .onFailure { error ->
                        Log.e(TAG, "Failed to load user tags: ${error.message}")
                    }

                // 채팅 내역 로드
                loadMessageHistory(otherUserId)

                // 실시간 메시지 수신 시작
                startReceivingMessages(otherUserId)

                // 채팅 상태 확인
//                checkMessageStatus(otherUserId)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    private suspend fun loadMessageHistory(otherUserId: String) {
        _uiState.update { it.copy(isLoading = true) }

        try {
            val result = getMessageHistoryUseCase(otherUserId, currentCursor)
            result.onSuccess { cursorResult ->
                _messages.update { currentMessages ->
                    if (currentCursor == null) {
                        cursorResult.data
                    } else {
                        currentMessages + cursorResult.data
                    }
                }
                currentCursor = cursorResult.nextCursor
            }.onFailure { error ->
                _uiState.update { it.copy(error = error.message) }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message) }
        } finally {
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun sendMessage(otherUserId: String, message: String) {
        if (message.isBlank()) return

        _uiState.update { it.copy(isSending = true) }

        viewModelScope.launch {
            try {
                val result = sendMessageUseCase(otherUserId, message)
                result.onSuccess { sentMessage ->
                    _messages.update { currentMessages ->
                        listOf(sentMessage) + currentMessages
                    }
                    _shouldScrollToBottom.value = true
                }.onFailure { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                _uiState.update { it.copy(isSending = false) }
            }
        }
    }

    private fun startReceivingMessages(otherUserId: String) {
        viewModelScope.launch {
            try {
                receiveMessageUseCase(otherUserId).collect { message ->
                    _messages.update { currentMessages ->
                        listOf(message) + currentMessages
                    }
                    _shouldScrollToBottom.value = true
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun loadMoreMessages(otherUserId: String) {
        Log.d(TAG, "loadMoreMessages called. isLoading: ${_uiState.value.isLoading}, currentCursor: $currentCursor")
        if (_uiState.value.isLoading) {
            Log.d(TAG, "loadMoreMessages returned early: isLoading is true.")
            return
        }
        if (currentCursor == null) {
            Log.d(TAG, "loadMoreMessages returned early: currentCursor is null (no more data).")
            return
        }

        viewModelScope.launch {
            try {
                loadMessageHistory(otherUserId)
            } finally {
            }
        }
    }

    fun moveToPreviousChats(otherUserId: String) {
        viewModelScope.launch {
            try {
                val result = moveMessageToPreviousUseCase(otherUserId)
                result.onFailure { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun onScrollToBottomHandled() {
        _shouldScrollToBottom.value = false
    }
} 