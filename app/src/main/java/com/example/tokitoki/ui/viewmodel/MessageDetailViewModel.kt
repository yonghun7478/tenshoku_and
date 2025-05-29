package com.example.tokitoki.ui.viewmodel

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
    private val moveMessageToPreviousUseCase: MoveMessageToPreviousUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessageDetailUiState())
    val uiState: StateFlow<MessageDetailUiState> = _uiState.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private var currentCursor: String? = null
    private var isLoadingMore = false

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

    private fun loadMessageHistory(otherUserId: String) {
        viewModelScope.launch {
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
    }

    fun sendMessage(otherUserId: String, message: String) {
        if (message.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true) }

            try {
                val result = sendMessageUseCase(otherUserId, message)
                result.onSuccess {
                    // 메시지 전송 성공 시 상태 업데이트
                    updateMessageStatusUseCase(otherUserId, true)
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
                        currentMessages + message
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun loadMoreMessages(otherUserId: String) {
        if (isLoadingMore || currentCursor == null) return

        isLoadingMore = true
        loadMessageHistory(otherUserId)
        isLoadingMore = false
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

//    private fun checkMessageStatus(otherUserId: String) {
//        viewModelScope.launch {
//            val result = updateMessageStatusUseCase.getMessageStatus(otherUserId)
//            result.onSuccess { messageStatus ->
//                _uiState.update { it.copy(messageStatus = messageStatus) }
//            }
//        }
//    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
} 