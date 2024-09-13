package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.ui.constants.AgreementConfirmationAction
import com.example.tokitoki.ui.state.AgreementConfirmationEvent
import com.example.tokitoki.ui.state.AgreementConfirmationState
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
class AgreementConfirmationViewModel @Inject constructor(
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<AgreementConfirmationEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _uiState = MutableStateFlow(AgreementConfirmationState())
    val uiState: StateFlow<AgreementConfirmationState> = _uiState.asStateFlow()

    fun initState() {
        _uiState.value = AgreementConfirmationState()
    }

    fun onAgreementChange(isAgeConfirmed: Boolean = false) {
        _uiState.update {
            if (isAgeConfirmed) {
                it.copy(isAgeConfirmed = !_uiState.value.isAgeConfirmed)
            } else {
                it.copy(isPolicyConfirmed = !_uiState.value.isPolicyConfirmed)
            }
        }
    }

    fun agreementConfirmationAction(action: AgreementConfirmationAction) {
        viewModelScope.launch {
            _uiEvent.emit(AgreementConfirmationEvent.ACTION(action))
        }
    }

    fun validateAgreement(): Boolean {
        return _uiState.value.isAgeConfirmed && _uiState.value.isPolicyConfirmed
    }

    fun updateShowDialogState(showDialog: Boolean) {
        _uiState.update {
            if(showDialog)
                it.copy(showDialog = true)
            else
                it.copy(showDialog = false)
        }
    }
}