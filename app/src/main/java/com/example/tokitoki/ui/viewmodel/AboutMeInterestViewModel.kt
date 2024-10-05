package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.ui.constants.AboutMeInterestAction
import com.example.tokitoki.ui.state.AboutMeInterestEvent
import com.example.tokitoki.ui.state.AboutMeInterestState
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
class AboutMeInterestViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(AboutMeInterestState())
    val uiState: StateFlow<AboutMeInterestState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AboutMeInterestEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val tabs: List<String> = listOf("趣味", "ライフスタイル", "価値観")

    fun init() {
        _uiState.value = AboutMeInterestState()
    }

    fun aboutMeInterestAction(action: AboutMeInterestAction) {
        viewModelScope.launch {
            _uiEvent.emit(AboutMeInterestEvent.ACTION(action))
        }
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