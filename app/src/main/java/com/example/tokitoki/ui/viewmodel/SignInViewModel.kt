package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.ui.state.SignInEvent
import com.example.tokitoki.ui.constants.SignInAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {
    private val _uiEvent = MutableSharedFlow<SignInEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun clickListener(action: SignInAction) {
        viewModelScope.launch {
            _uiEvent.emit(SignInEvent.ACTION(action))
        }
    }
}