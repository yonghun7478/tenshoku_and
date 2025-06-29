package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tokitoki.ui.state.MainUiEvent
import com.example.tokitoki.ui.state.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    // UI StateFlow: 화면 상태 유지
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    // UI Event SharedFlow: 이벤트 전달
    private val _uiEvent = MutableSharedFlow<MainUiEvent>()
    val uiEvent: SharedFlow<MainUiEvent> = _uiEvent.asSharedFlow()

    fun onEvent(event: MainUiEvent) {
        when (event) {
            is MainUiEvent.SelectBottomItem -> {
                _uiState.value = _uiState.value.copy(selectedBottomItem = event.item)
            }
        }
    }
}