package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.ValidateEmailFormatUseCase
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
class RegisterWithEmailViewModel @Inject constructor(
    private val validateEmailFormatUseCase: ValidateEmailFormatUseCase
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<RegisterWithEmailEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _uiState = MutableStateFlow(RegisterWithEmailState())
    val uiState: StateFlow<RegisterWithEmailState> = _uiState.asStateFlow()

    fun initState() {
        _uiState.value = RegisterWithEmailState()
    }

    fun registerWithEmailAction(action: RegisterWithEmailAction) {
        viewModelScope.launch {
            _uiEvent.emit(RegisterWithEmailEvent.ACTION(action))
        }
    }

    fun onEmailChanged(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun updateShowDialogState(showDialog: Boolean) {
        _uiState.update {
            it.copy(showDialog = showDialog)
        }
    }

    suspend fun validateEmail(email: String): Boolean {
        return validateEmailFormatUseCase.invoke(email)
    }
}