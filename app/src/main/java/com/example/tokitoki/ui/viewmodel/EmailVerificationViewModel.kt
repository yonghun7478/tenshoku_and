package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.ValidateAuthCodeUseCase
import com.example.tokitoki.ui.constants.EmailVerificationAction
import com.example.tokitoki.ui.state.EmailVerificationEvent
import com.example.tokitoki.ui.state.EmailVerificationState
import com.example.tokitoki.ui.state.RegisterWithEmailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailVerificationViewModel @Inject constructor(
    private val validateAuthCodeUseCase: ValidateAuthCodeUseCase,
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<EmailVerificationEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _uiState = MutableStateFlow(EmailVerificationState())
    val uiState: StateFlow<EmailVerificationState> = _uiState.asStateFlow()

    fun initState() {
        _uiState.value = EmailVerificationState()
    }

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

    suspend fun validateCode(code: String): Boolean {
        return validateAuthCodeUseCase.invoke(code)
    }

    fun updateShowDialogState(showDialog: Boolean) {
        _uiState.update {
            if(showDialog)
                it.copy(showDialog = true)
            else
                it.copy(showDialog = false, code = "")
        }
    }
}
