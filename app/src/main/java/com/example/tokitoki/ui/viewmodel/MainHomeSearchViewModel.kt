package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.ui.state.MainHomeSearchUiEvent
import com.example.tokitoki.ui.state.MainHomeSearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainHomeSearchViewModel @Inject constructor(
) : ViewModel() {
    // UI 상태(StateFlow)
    private val _uiState = MutableStateFlow(MainHomeSearchUiState())
    val uiState: StateFlow<MainHomeSearchUiState> = _uiState.asStateFlow()

    // UI 이벤트(SharedFlow)
    private val _uiEvent = MutableSharedFlow<MainHomeSearchUiEvent>()
    val uiEvent: SharedFlow<MainHomeSearchUiEvent> = _uiEvent.asSharedFlow()

    fun onEvent(event: MainHomeSearchUiEvent) {
        when (event) {
            is MainHomeSearchUiEvent.OrderSelected -> {
                _uiState.value = _uiState.value.copy(orderType = event.orderType)
            }
            is MainHomeSearchUiEvent.UserSelected -> {
                viewModelScope.launch {
                    _uiEvent.emit(event)
                }
            }
        }
    }
}