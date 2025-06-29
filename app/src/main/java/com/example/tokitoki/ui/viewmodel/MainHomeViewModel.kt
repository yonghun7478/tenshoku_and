package com.example.tokitoki.ui.viewmodel

import com.example.tokitoki.ui.state.MainHomeUiEvent
import com.example.tokitoki.ui.state.MainHomeUiState
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainHomeViewModel @Inject constructor() : ViewModel() {

    // UI 상태(StateFlow)
    private val _uiState = MutableStateFlow(MainHomeUiState())
    val uiState: StateFlow<MainHomeUiState> = _uiState.asStateFlow()

    // UI 이벤트(SharedFlow)
    private val _uiEvent = MutableSharedFlow<MainHomeUiEvent>()
    val uiEvent: SharedFlow<MainHomeUiEvent> = _uiEvent.asSharedFlow()

    fun onEvent(event: MainHomeUiEvent) {
        when (event) {
            is MainHomeUiEvent.TabSelected -> {
                _uiState.value = _uiState.value.copy(selectedTab = event.tab)
            }
        }
    }
}