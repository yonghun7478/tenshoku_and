package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.ui.constants.AboutMeNameAction
import com.example.tokitoki.ui.state.AboutMeNameEvent
import com.example.tokitoki.ui.state.AboutMeNameState
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
class AboutMeNameViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(AboutMeNameState())
    val uiState: StateFlow<AboutMeNameState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AboutMeNameEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun init() {
        _uiState.value = AboutMeNameState()
    }

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun aboutMeNameAction(action: AboutMeNameAction) {
        viewModelScope.launch {
            _uiEvent.emit(AboutMeNameEvent.ACTION(action))
        }
    }

    fun checkName() = _uiState.value.name.isNotEmpty()

    fun updateShowDialogState(showDialog: Boolean) {
        _uiState.update {
            if(showDialog)
                it.copy(showDialog = true)
            else
                it.copy(showDialog = false)
        }
    }
}