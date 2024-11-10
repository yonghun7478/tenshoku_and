package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tokitoki.ui.state.AboutMeProfInputEvent
import com.example.tokitoki.ui.state.AboutMeProfInputState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AboutMeProfInputViewModel
@Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(AboutMeProfInputState())
    val uiState: StateFlow<AboutMeProfInputState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AboutMeProfInputEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
}