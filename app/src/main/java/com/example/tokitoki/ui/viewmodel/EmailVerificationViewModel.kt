package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.ValidateAuthCodeUseCase
import com.example.tokitoki.ui.constants.EmailVerificationAction
import com.example.tokitoki.ui.state.EmailVerificationEvent
import com.example.tokitoki.ui.state.EmailVerificationState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class EmailVerificationViewModel @Inject constructor(
    private val validateAuthCodeUseCase: ValidateAuthCodeUseCase,
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<EmailVerificationEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _uiState = MutableStateFlow(EmailVerificationState())
    val uiState: StateFlow<EmailVerificationState> = _uiState.asStateFlow()

    fun onPasswordValueChange(newText: String) {
        if (newText.length <= 6) {
            _uiState.value = _uiState.value.copy(code = newText)
        }
    }

    fun emailVerificationAction(action: EmailVerificationAction) {
        viewModelScope.launch {
            _uiEvent.emit(EmailVerificationEvent.ACTION(action))
        }
    }

    fun validateCode(code: String) {
        viewModelScope.launch {
            val result = validateAuthCodeUseCase.invoke(code)
            if (result) {
                _uiEvent.emit(EmailVerificationEvent.ACTION(EmailVerificationAction.SUCCESS))
            }
        }
    }
}
