package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.ui.constants.RegisterWithEmailAction
import com.example.tokitoki.ui.state.RegisterWithEmailEvent
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
class RegisterWithEmailViewModel @Inject constructor() : ViewModel() {
    private val _uiEvent = MutableSharedFlow<RegisterWithEmailEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _uiState = MutableStateFlow(RegisterWithEmailState())
    val uiState: StateFlow<RegisterWithEmailState> = _uiState.asStateFlow()

    fun clickListener(action: RegisterWithEmailAction) {
        viewModelScope.launch {
            _uiEvent.emit(RegisterWithEmailEvent.ACTION(action))
        }
    }

    private fun validateEmail(email: String): Boolean {
        // 이메일 형식을 검증하는 정규 표현식
        val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")

        // 입력된 이메일 문자열이 정규 표현식과 일치하는지 확인
        return emailPattern.matches(email)
    }

    fun onEmailChanged(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun submit(email: String) {
        viewModelScope.launch {
            val isValidEmail = validateEmail(email)

            if (isValidEmail) {
                _uiState.update {
                    it.copy(showDialog = false)
                }
            } else {
                _uiState.update {
                    it.copy(showDialog = true)
                }
            }
        }
    }

    fun onDismiss() {
        _uiState.update {
            it.copy(showDialog = false)
        }
    }
}