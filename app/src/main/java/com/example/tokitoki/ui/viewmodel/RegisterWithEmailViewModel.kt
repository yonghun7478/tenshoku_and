package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.ui.constants.RegisterWithEmailAction
import com.example.tokitoki.ui.state.RegisterWithEmailEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterWithEmailViewModel @Inject constructor() : ViewModel() {
    private val _uiEvent = MutableSharedFlow<RegisterWithEmailEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun clickListener(action: RegisterWithEmailAction) {
        viewModelScope.launch {
            _uiEvent.emit(RegisterWithEmailEvent.ACTION(action))
        }
    }
}